import exceptions.FileReadException;
import exceptions.InvalidFileFormatException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Translator — использует Dictionary для перевода входного текста.
 *
 * Поведение:
 *  - регистр игнорируется при поиске ключей (в словаре хранятся ключи в lower-case)
 *  - если найдено несколько совпадений — выбирается вариант с максимальным количеством слов в ключе
 *  - при совпадении сохраняется пунктуация, стоящая в конце последнего слова фразы
 *  - неизвестные "чистые" слова собираются в множество unknownWords (в нижнем регистре)
 */
public class Translator {

    private final Dictionary dictionary;
    private final Set<String> unknownWords = new TreeSet<>(); // отсортированный набор для стабильности

    /**
     * Создаёт переводчик и загружает словарь по указанному пути
     *
     * @param dictionaryFile путь к файлу словаря (.txt)
     * @throws FileReadException при проблемах чтения файла
     * @throws InvalidFileFormatException при ошибках формата
     */
    public Translator(String dictionaryFile) throws FileReadException, InvalidFileFormatException {
        this.dictionary = new Dictionary(dictionaryFile);
        System.out.println("Словарь загружен. Записей: " + dictionary.size());
    }

    /**
     * Основной метод перевода одной строки
     *
     * @param inputText исходный текст
     * @return переведённая строка
     */
    public String translate(String inputText) {
        if (inputText == null || inputText.trim().isEmpty()) {
            return inputText;
        }

        // разделяем по пробелам
        String[] tokens = inputText.split("\\s+");
        StringBuilder result = new StringBuilder();

        int i = 0;
        while (i < tokens.length) {
            Match match = findLongestMatch(tokens, i);

            if (match != null) {
                // добавляем пробел между элементами результата
                if (result.length() > 0) result.append(" ");
                result.append(match.translationWithPunctuation);
                i += match.wordCount;
            } else {
                // нет совпадения — проверяем, надо ли занести слово в unknownWords
                if (result.length() > 0) result.append(" ");
                result.append(tokens[i]);

                String clean = cleanWord(tokens[i]);
                if (!clean.isEmpty() && isAlnumWord(clean)) {
                    String low = clean.toLowerCase(Locale.ROOT);
                    if (!dictionary.getEntries().containsKey(low)) {
                        unknownWords.add(low);
                    }
                }
                i++;
            }
        }

        return result.toString();
    }

    /**
     * Поиск наилучшего (длинного) совпадения, начиная с позиции startIndex в массиве токенов.
     * Возвращает объект Match с переводом (и сохранённой пунктуацией) и количеством слов, которые закрывает совпадение.
     */
    private Match findLongestMatch(String[] tokens, int startIndex) {
        String bestTranslation = null;
        int bestWordCount = 0;
        String bestPunctuation = "";

        // Строим фразу по словам: от startIndex до j
        StringBuilder phraseBuilder = new StringBuilder();
        for (int j = startIndex; j < tokens.length; j++) {
            if (phraseBuilder.length() > 0) phraseBuilder.append(" ");

            // для всех слов внутри фразы используем очищенную форму (без ведущих/концевых небуквенных символов)
            String clean = cleanWord(tokens[j]).toLowerCase(Locale.ROOT);
            phraseBuilder.append(clean);

            String candidate = phraseBuilder.toString().replaceAll("\\s+", " ").trim();

            if (candidate.isEmpty()) {
                // если чистые части пусты (например токен только пунктуация), прекращаем расширение
                break;
            }

            // проверка прямой фразы
            if (dictionary.getEntries().containsKey(candidate)) {
                // количество слов в фразе = j - startIndex + 1
                int wc = j - startIndex + 1;
                // выбираем большую по словарной длине (числу слов)
                if (wc > bestWordCount) {
                    bestWordCount = wc;
                    bestTranslation = dictionary.getEntries().get(candidate);
                    bestPunctuation = trailingPunctuation(tokens[j]);
                }
            }
        }

        if (bestTranslation != null) {
            String translationWithPunct = bestTranslation + bestPunctuation;
            return new Match(translationWithPunct, bestWordCount);
        }

        return null;
    }

    /**
     * Извлекает конечную пунктуацию (если есть) из токена.
     * Например: "window," -> "," ; "hello!!" -> "!!"; "foo" -> ""
     * 
     * @param inputText исходный токен
     * @return переведённая строка
     */
    private String trailingPunctuation(String token) {
        if (token == null || token.isEmpty()) return "";
        int idx = token.length() - 1;
        while (idx >= 0 && !isLetterOrDigit(token.charAt(idx))) {
            idx--;
        }
        return token.substring(idx + 1); // может быть ""
    }

    /**
     * Удаляет ведущие и завершающие не-алфавитно-числовые символы.
     * Сохраняет буквы любого языка и цифры.
     *
     * Примеры:
     *   "\"hello,\"" -> "hello"
     *   "(привет)" -> "привет"
     * 
     * @param word исходное слово
     * @return переведённая строка
     */
    private String cleanWord(String word) {
        if (word == null) return "";
        // удаляем ведущие небуквенно-цифровые символы
        String s = word.replaceAll("^([^\\p{L}\\p{N}])+",""); // начало
        // и завершающие небуквенно-цифровые символы
        s = s.replaceAll("([^\\p{L}\\p{N}])+$",""); // конец
        return s;
    }

    private boolean isLetterOrDigit(char c) {
        return Character.isLetterOrDigit(c);
    }

    /**
     * Проверяет, состоит ли слово только из букв/цифр (после очистки)
     * 
     * @param word исходное слово
     * @return bool
     */
    private boolean isAlnumWord(String word) {
        if (word == null || word.isEmpty()) return false;
        return word.codePoints().allMatch(cp -> Character.isLetterOrDigit(cp));
    }

    /**
     * Возвращает множество собранных неизвестных слов (нижний регистр)
     */
    public Set<String> getUnknownWords() {
        return Collections.unmodifiableSet(unknownWords);
    }

    /**
     * Сохраняет неизвестные слова в файл output/unknown_words.txt в формате "word | "
     * Сначала читаем уже имеющиеся записи и не добавляем дубли
     */
    public void saveUnknownWords() {
        if (unknownWords.isEmpty()) {
            System.out.println("Непереведённых слов не найдено");
            return;
        }

        try {
            Path outDir = Paths.get("output");
            if (!Files.exists(outDir)) {
                Files.createDirectories(outDir);
            }

            Path outFile = outDir.resolve("unknown_words.txt");
            Set<String> existing = new HashSet<>();

            if (Files.exists(outFile)) {
                List<String> lines = Files.readAllLines(outFile, StandardCharsets.UTF_8);
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) continue;
                    String[] parts = trimmed.split("\\|", 2);
                    if (parts.length > 0) {
                        String w = parts[0].trim();
                        if (!w.isEmpty()) existing.add(w.toLowerCase(Locale.ROOT));
                    }
                }
            }

            List<String> toAppend = new ArrayList<>();
            for (String w : unknownWords) {
                if (!existing.contains(w)) {
                    toAppend.add(w + " | ");
                }
            }

            if (!toAppend.isEmpty()) {
                Files.write(outFile, toAppend, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Добавлено новых непереведённых слов: " + toAppend.size());
                System.out.println("Файл с непереведёнными словами: " + outFile.toString());
            } else {
                System.out.println("Новые непереведённые слова отсутствуют (всё уже записано)");
            }

        } catch (IOException e) {
            System.err.println("Ошибка при сохранении непереведённых слов: " + e.getMessage());
        }
    }

    /**
     * Вложенный класс-результат поиска совпадения
     */
    private static class Match {
        final String translationWithPunctuation;
        final int wordCount;

        Match(String translationWithPunctuation, int wordCount) {
            this.translationWithPunctuation = translationWithPunctuation;
            this.wordCount = wordCount;
        }
    }
}
