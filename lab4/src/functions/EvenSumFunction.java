package functions;

import java.util.Arrays;

/**
 * Класс вычисляет сумму чётных чисел
 * Используется только Stream API
 */
public class EvenSumFunction {

    /**
     * Возвращает сумму всех чётных элементов массива
     *
     * @param numbers массив целых чисел
     * @return сумма чётных значений или 0, если их нет
     */
    public int getEvenSum(final int[] numbers) {
        return Arrays.stream(numbers)
                .filter(value -> value % 2 == 0)
                .sum();
    }
}
