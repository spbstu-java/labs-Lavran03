package exceptions;

/**
 * Исключение при неверном формате строки в словаре.
 * Ожидается формат: "лево | право"
 */
public final class InvalidFileFormatException extends Exception {

    public InvalidFileFormatException(String message) {
        super(message);
    }

    public InvalidFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}