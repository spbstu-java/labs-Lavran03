package strategy;

/**
 * Класс для стратегии передвижения вплавь
 */
public class SwimStrategy implements MoveStrategy {

    @Override
    public int move() {
        System.out.println("Герой плывёт");
        return 8;
    }

    @Override
    public String getName() {
        return "Вплавь";
    }
}
