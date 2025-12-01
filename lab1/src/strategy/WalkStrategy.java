package strategy;

/**
 * Класс для стратегии передвижения пешком
 */
public class WalkStrategy implements MoveStrategy {

    @Override
    public int move() {
        System.out.println("Герой идёт пешком");
        return 5;
    }

    @Override
    public String getName() {
        return "Пешком";
    }
}
