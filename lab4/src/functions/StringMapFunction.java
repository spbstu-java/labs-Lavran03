package functions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс предназначеный для преобразования список строк в Map
 * Ключом является первый символ строки, значением — остальные символы
 * Используется только Stream API
 */
public class StringMapFunction {

    /**
     * Преобразует строки в Map
     *
     * @param values список строк
     * @return Map с ключом-первым символом и значением-остатком строки
     */
    public Map<Character, String> convertToMap(final List<String> values) {
        return values.stream()
                .collect(Collectors.toMap(
                        value -> value.charAt(0),
                        value -> value.length() > 1 ? value.substring(1) : ""
                ));
    }
}
