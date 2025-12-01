package functions;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Класс преобразует строки в верхний регистр и добавляет префикс "_new_"
 * Используется только Stream API
 */
public class UpperCaseFunction {

    /**
     * Преобразует элементы списка в формат: _new_<строка>
     *
     * @param strings исходный список строк
     * @return изменённый список
     */
    public List<String> transform(final List<String> strings) {
        return strings.stream()
                .map(value -> "_new_" + value.toUpperCase(Locale.ROOT))
                .collect(Collectors.toList());
    }
}
