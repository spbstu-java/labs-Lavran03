import exceptions.FileReadException;
import exceptions.InvalidFileFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dictionary загружает пары "фраза | перевод" из файла и хранит их в памяти
 * Ключи нормализуются: trim + toLowerCase
 */
public class Dictionary {

    private final Map<String, String> entries = new LinkedHashMap<>();

    /**
     * Загружает словарь из файла при создании
     *
     * @param filename путь к файлу словаря (*.txt)
     * @throws FileReadException             при проблемах с IO / файлом
     * @throws InvalidFileFormatException    при неверном формате строки
     */
    public Dictionary(String filename) throws FileReadException, InvalidFileFormatException {
        loadFromFile(filename);
    }

    private void loadFromFile(String filename) throws FileReadException, InvalidFileFormatException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNo = 1;
            boolean any = false;

            while ((line = reader.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) {
                    lineNo++;
                    continue;
                }

                String[] parts = raw.split("\\|", 2);
                if (parts.length != 2) {
                    throw new InvalidFileFormatException(
                            "Неверный формат в строке " + lineNo + ": \"" + line + "\". Ожидается 'лево | право'");
                }

                String left = parts[0].trim();
                String right = parts[1].trim();

                if (left.isEmpty() || right.isEmpty()) {
                    throw new InvalidFileFormatException(
                            "Пустая часть слова или перевода в строке " + lineNo + ": \"" + line + "\"");
                }

                // нормализация ключа: пробелы сводим, нижний регистр
                String normKey = normalizeKey(left);
                entries.put(normKey, right);
                any = true;
                lineNo++;
            }

            if (!any) {
                throw new InvalidFileFormatException("Файл словаря пуст или не содержит пар 'лево | право'");
            }

        } catch (IOException ioe) {
            throw new FileReadException("Ошибка при чтении файла \"" + filename + "\": " + ioe.getMessage(), ioe);
        }
    }

    /**
     * Нормализует ключ словаря: убирает лишние пробелы и приводит к нижнему регистру
     */
    private String normalizeKey(String s) {
        return s.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    /**
     * Возвращает карту словаря (не для модификации)
     */
    public Map<String, String> getEntries() {
        return entries;
    }

    /**
     * Возвращает количество записей в словаре
     */
    public int size() {
        return entries.size();
    }
}
