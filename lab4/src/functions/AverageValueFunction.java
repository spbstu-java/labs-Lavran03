package functions;

import java.util.List;

/**
 * Класс предназначен для вычисления среднего значения чисел
 * Используется только Stream API
 */
public class AverageValueFunction {

    /**
     * Возвращает среднее значение элементов списка
     *
     * @param numbers список целых чисел
     * @return среднее арифметическое всех чисел списка
     */
    public double calculateAverage(final List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
}
