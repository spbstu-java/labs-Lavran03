import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс ReflectionExecutor для вызова protected и private методов,
 * помеченных аннотацией {@link Repeat}
 */
public class ReflectionExecutor {

    /**
     * Значения по умолчанию для поддерживаемых типов параметров
     * Ключ — тип параметра, значение — объект, который будет подставлен при вызове
     */
    private static final Map<Class<?>, Object> paramValuesMap = new HashMap<>();

    static {
        paramValuesMap.put(String.class, "default");
        paramValuesMap.put(int.class, 1);
        paramValuesMap.put(Integer.class, 1);
        paramValuesMap.put(double.class, 1.0);
        paramValuesMap.put(Double.class, 1.0);
        paramValuesMap.put(boolean.class, true);
        paramValuesMap.put(Boolean.class, true);
    }

    /**
     * Метож находит у {@code targetObject} все методы с модификаторами protected или
     * private, которые помечены аннотацией {@link Repeat}, и вызывает каждый из них
     * {@code n} раз, где {@code n} берётся из аннотации
     *
     * @param targetObject объект, у которого нужно найти и выполнить методы
     */
    public static void executeProtectedAndPrivateAnnotatedMethods(Object targetObject) {
        if (targetObject == null)
            return;

        Class<?> targetType = targetObject.getClass();
        Method[] declaredMethods = targetType.getDeclaredMethods();

        for (Method methodHandle : declaredMethods) {
            int modifier = methodHandle.getModifiers();
            boolean isProtected = Modifier.isProtected(modifier);
            boolean isPrivate = Modifier.isPrivate(modifier);

            // обрабатываем только методы protected и private методы
            if (!isProtected && !isPrivate) {
                continue;
            }

            // обрабатываем только методы помеченные аннотацией Repeat
            if (!methodHandle.isAnnotationPresent(Repeat.class)) {
                continue;
            }

            Repeat repeatAnnotation = methodHandle.getAnnotation(Repeat.class);
            int repeatCount = repeatAnnotation == null ? 1 : repeatAnnotation.value();

            System.out.println("Надденый метод: " + methodHandle.getName()
                    + " | модификатор: " + modifierName(modifier) + " | , @Repeat(" + repeatCount + ")");

            boolean accessChanged = false;
            try {
                // делаем метод доступным, если нужно
                if (!methodHandle.canAccess(targetObject)) {
                    methodHandle.setAccessible(true);
                    accessChanged = true;
                }

                Object[] args = buildArgsForMethod(methodHandle);

                for (int i = 0; i < repeatCount; i++) {
                    System.out.print("Вызов " + (i + 1) + "/" + repeatCount + ": ");
                    try {
                        Object result = methodHandle.invoke(targetObject, args);
                        if (!methodHandle.getReturnType().equals(void.class)) {
                            System.out.println("Возвращено: " + result);
                        }
                    } catch (InvocationTargetException ite) {
                        System.err.println("Исключение в вызываемом методе: " + ite.getTargetException());
                    }
                }

            } catch (IllegalAccessException | IllegalArgumentException e) {
                System.err.println("Ошибка при вызове метода " + methodHandle.getName() + ": " + e.getMessage());
            } finally {
                if (accessChanged) {
                    try {
                        methodHandle.setAccessible(false);
                    } catch (SecurityException ignored) {
                        // восстановить доступ не удалось — игнорируем
                    }
                }
            }

            System.out.println();
        }
    }

    /**
     * Метод для построения массива аргументов для метода на основе типов параметров
     * Если типа нету в списке поддерживаемых — в позицию подставляется null
     *
     * @param methodHandle дескриптор метода
     * @return массив аргументов длины, равной количеству параметров метода. Возможно
     * передать пустой массив при отсутствии аргументов
     */
    private static Object[] buildArgsForMethod(Method methodHandle) {
        Class<?>[] paramTypes = methodHandle.getParameterTypes();
        if (paramTypes.length == 0)
            return new Object[0];

        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Object defaultValue = paramValuesMap.get(paramTypes[i]);
            if (defaultValue == null) {
                System.err
                        .println("Для данного тип уотсутствует значение по умолчанию: " + paramTypes[i].getName()
                                + " — будет использован null");
            }
            args[i] = defaultValue;
        }
        return args;
    }

    /**
     * Метод, возврвщающий имя модификатора доступа
     *
     * @param modifier битовый набор модификаторов
     * @return "private" | "protected" | "public" | "package-private"
     */
    private static String modifierName(int modifier) {
        if (Modifier.isPrivate(modifier)) {
            return "private";
        }
        if (Modifier.isProtected(modifier)) {
            return "protected";
        }
        if (Modifier.isPublic(modifier)) {
            return "public";
        }
        return "package-private";
    }
}
