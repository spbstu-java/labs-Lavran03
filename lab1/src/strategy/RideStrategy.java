package strategy;

/**
 * Класс для стратегии передвижения верхом
 */
public class RideStrategy implements MoveStrategy {
    
    @Override
    public int move() {
        System.out.println("Герой скачет на лошади");
        return 15;
    }

    @Override
    public String getName() {
        return "Верхом";
    }
}
