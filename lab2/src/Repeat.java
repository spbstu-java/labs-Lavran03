import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, указывающая сколько раз следует вызвать помеченный метод
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Repeat {
    /**
     * Количество повторов вызова аннотированного метода
     * 
     * @return количество повторов (целое число, >=1 желательное)
     */
    int value();
}