import functions.AverageValueFunction;
import functions.UpperCaseFunction;
import functions.UniqueSquaresFunction;
import functions.LastElementFunction;
import functions.EvenSumFunction;
import functions.StringMapFunction;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== Демонстрация работы функций Stream API ===");

        AverageValueFunction averageValueFunction = new AverageValueFunction();
        UpperCaseFunction upperCaseFunction = new UpperCaseFunction();
        UniqueSquaresFunction uniqueSquaresFunction = new UniqueSquaresFunction();
        LastElementFunction lastElementFunction = new LastElementFunction();
        EvenSumFunction evenSumFunction = new EvenSumFunction();
        StringMapFunction stringMapFunction = new StringMapFunction();

        List<Integer> nums = Arrays.asList(11, 62, 11, 17, 11, 9);
        List<String> strings = Arrays.asList("This", "is", "java!");

        System.out.println("\nСреднее значение: " + averageValueFunction.calculateAverage(nums));
        System.out.println("\nСтроки в верхнем регистре: " + upperCaseFunction.transform(strings));
        System.out.println("\nКвадраты уникальных чисел: " + uniqueSquaresFunction.getUniqueSquares(nums));
        System.out.println("\nПоследний элемент: " + lastElementFunction.getLast(nums));
        System.out.println("\nСумма чётных: " + evenSumFunction.getEvenSum(new int[]{1, 2, 3, 4, 6}));
        System.out.println("\nСтроки в Map: " + stringMapFunction.convertToMap(strings));
        
        // Демонстрация ошибки — получение последнего элемента из пустого списка
        try {
          System.out.println("\nПоследний элемент (пустой массив): " + lastElementFunction.getLast(Arrays.asList()));
      } catch (IllegalArgumentException exception) {
          System.out.println("\nОшибка: " + exception.getMessage());
      }
    }
}
