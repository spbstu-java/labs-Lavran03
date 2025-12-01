package hero;

import strategy.MoveStrategy;

/**
 * Класс Hero хранит текущую стратегию перемещения и позволяет
 */
public class Hero {
    private MoveStrategy strategy;

    /**
     * Конструктор с возможностью задать начальную стратегию
     *
     * @param initialStrategy начальная стратегия, может быть null
     */
    public Hero(MoveStrategy initialStrategy) {
        this.strategy = initialStrategy;
    }

    /**
     * Устанавливает стратегию перемещения. После этого выводит имя стратегии
     *
     * @param strategy новая стратегия
     */
    public void setStrategy(MoveStrategy strategy) {
        this.strategy = strategy;
        System.out.println("Стратегия изменена на: " + strategy.getName());
    }

    /**
     * Выполнить шаг согласно текущей стратегии
     *
     * @return расстояние, или 0 если стратегия не установлена
     */
    public int step() {
        if (strategy == null) {
            System.out.println("Стратегия не задана");
            return 0;
        }
        return strategy.move();
    }
    
    /**
     * Получить имя текущей стратегии
     *
     * @return имя стратегии или "нет" если стратегия не установлена
     */
    public String currentStrategyName() {
        return strategy == null ? "нет" : strategy.getName();
    }
}