import exceptions.FileReadException;
import exceptions.InvalidFileFormatException;

import java.util.Scanner;

/*
    Пример содержимого файла dictionary.txt:
        look | смотреть
        look forward | ожидать
        window | окно

    Пример запуска (если не передавать аргументы, будет использован "dictionary.txt"):
        java Main dictionary.txt

    Пример ввода (после запуска):
        dog look to the window, dog LOOK forward!!
        exit

    Ожидаемый вывод:
        Перевод: dog смотреть to the окно, dog ожидать!!
*/

public final class Main {

    public static void main(String[] args) {

        final String dictPath = (args.length >= 1) ? args[0] : "dictionary.txt";

        Translator translator;
        try {
            System.out.println("Загружаем словарь из: " + dictPath);
            translator = new Translator(dictPath);
        } catch (InvalidFileFormatException e) {
            System.err.println("Ошибка формата словаря: " + e.getMessage());
            return;
        } catch (FileReadException e) {
            System.err.println("Ошибка чтения словаря: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Переводчик запущен. Введите текст для перевода (введите 'exit' или 'выход' для завершения):");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line == null) break;
            String trimmed = line.trim();
            if (trimmed.equalsIgnoreCase("exit") || trimmed.equalsIgnoreCase("выход")) {
                // перед выходом — сохранить непереведённые слова
                translator.saveUnknownWords();
                System.out.println("До свидания!");
                break;
            }

            if (trimmed.isEmpty()) {
                continue;
            }

            String translated = translator.translate(line);
            System.out.println("Перевод: " + translated);
        }

        scanner.close();
    }
}
