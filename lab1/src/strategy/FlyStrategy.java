package strategy;

/**
 * Класс для стратегии передвижения летя
 */
public class FlyStrategy implements MoveStrategy {

    @Override
    public int move() {
        System.out.println("Герой летит");
        return 40;
    }
    
    @Override
    public String getName() {
        return "Летя";
    }
}
