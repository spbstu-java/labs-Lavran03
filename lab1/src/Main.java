import hero.Hero;
import strategy.MoveStrategy;
import strategy.WalkStrategy;
import strategy.SwimStrategy;
import strategy.FlyStrategy;
import strategy.RideStrategy;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // расстояние до цели
        int totalDistance = 30;
        Hero hero = new Hero(null);

        // создаем массив доступных стратегий
        MoveStrategy[] strategies = new MoveStrategy[] {
            new WalkStrategy(),
            new SwimStrategy(),
            new FlyStrategy(),
            new RideStrategy(),
        };

        Scanner scanner = new Scanner(System.in);

        while (totalDistance > 0) {
            System.out.println("\nДо пункта назначения осталось " + totalDistance + " км");
            System.out.println("Доступные стратегии:");
            // Выводим имена стратегий с нумерацией
            for (int i = 0; i < strategies.length; i++) {
                System.out.println((i + 1) + ". " + strategies[i].getName());
            }
            System.out.println("0. Выйти");

            System.out.print("Выберите стратегию: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод");
                continue;
            }

            if (choice == 0) {
                System.out.println("Выход из программы");
                break;
            } else if (choice < 0 || choice > strategies.length) {
                System.out.println("Некорректный номер стратегии");
                continue;
            }

            hero.setStrategy(strategies[choice - 1]);
            int moved = hero.step();
            totalDistance -= Math.min(moved, totalDistance);
            System.out.println("Пройдено " + moved + " км, осталось " + totalDistance + " км");
        }

        if (totalDistance == 0) {
            System.out.println("Герой достиг пункта назначения!");
        }

        scanner.close();
    }
}
