public class Main {
    public static void main(String[] args) {
        Welder welder = new Welder();

        System.out.println("-- Прямой вызов публичных методов --");
        welder.startWeld("сталь");
        welder.estimateHeat(120, 3.0);
        welder.status();

        System.out.println();
        System.out.println("-- Вызов аннотированных защищённых и приватных методов --");
        ReflectionExecutor.executeProtectedAndPrivateAnnotatedMethods(welder);
    }
}