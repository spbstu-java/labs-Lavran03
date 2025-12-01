package functions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс возвращающий квадраты элементов, которые встречаются только один раз
 * Используется только Stream API
 */
public class UniqueSquaresFunction {

    /**
     * Находит уникальные значения и возводит их в квадрат
     *
     * @param numbers список целых чисел
     * @return список квадратов уникальных элементов
     */
    public List<Integer> getUniqueSquares(final List<Integer> numbers) {
        return numbers.stream()
                .filter(value -> numbers.stream().filter(v -> v.equals(value)).count() == 1)
                .map(value -> value * value)
                .collect(Collectors.toList());
    }
}
