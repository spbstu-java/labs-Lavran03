package exceptions;

/**
 * Исключение для ошибок чтения файла:
 * отсутствует файл, нет доступа, ошибка IO и т.п.
 */
public final class FileReadException extends Exception {

    public FileReadException(String message) {
        super(message);
    }

    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }
}