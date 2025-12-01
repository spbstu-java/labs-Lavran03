package functions;

import java.util.Collection;

/**
 * Класс возвращающий последний элемент коллекции
 * При пустой коллекции выбрасывается исключение
 * Используется только Stream API
 */
public class LastElementFunction {

    /**
     * Возвращает последний элемент
     *
     * @param values коллекция
     * @param <T> тип элементов коллекции
     * @return последний элемент
     * @throws IllegalArgumentException при пустой коллекции
     */
    public <T> T getLast(final Collection<T> values) {
        return values.stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new IllegalArgumentException("Коллекция пуста"));
    }
}
