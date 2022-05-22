package ultimate.reflection;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A generic comparator that is comparing a random field of the given class. The field is either primitive or
 * {@link Comparable}. It is chosen during comparator instance creation and is used for all comparisons.
 * <p>
 * By default it compares only accessible fields, but this can be configured via a constructor property. If no field is
 * available to compare, the constructor throws {@link IllegalArgumentException}
 *
 * @param <T> the type of the objects that may be compared by this comparator
 */
public class RandomFieldComparator<T> implements Comparator<T> {

    private final Field sortField;

    public RandomFieldComparator(Class<T> targetType) {
        this(targetType, true);
    }

    /**
     * A constructor that accepts a class and a property indicating which fields can be used for comparison. If property
     * value is true, then only public fields or fields with public getters can be used.
     *
     * @param targetType                  a type of objects that may be compared
     * @param compareOnlyAccessibleFields config property indicating if only publicly accessible fields can be used
     */
    public RandomFieldComparator(Class<T> targetType, boolean compareOnlyAccessibleFields) {
        Predicate<Field> prelimFieldPredicate = field ->
                (Comparable.class.isAssignableFrom(field.getType()) || field.getType().isPrimitive());
        Predicate<Field> fieldPredicate;
        if (compareOnlyAccessibleFields) {
            Map<String, String> getters = Arrays.stream(targetType.getDeclaredMethods())
                    .filter(method -> (method.getName().startsWith("get") || method.getName().startsWith("is"))
                            && Modifier.isPublic(method.getModifiers()))
                    .map(method -> {
                        var methodName = method.getName();
                        if (methodName.startsWith("get")) {
                            return methodName.substring(3);
                        }
                        return methodName.substring(2);
                    }).collect(Collectors.toMap(Function.identity(), Function.identity()));

            Predicate<Field> methodsCheck = field -> (!Modifier.isPrivate(field.getModifiers()) ||
                    getters.containsKey(StringUtils.capitalize(field.getName())));

            fieldPredicate = prelimFieldPredicate.and(methodsCheck);
        } else {
            fieldPredicate = prelimFieldPredicate;
        }

        List<Field> classFields = Arrays.stream(targetType.getDeclaredFields())
                .filter(fieldPredicate)
                .toList();
        if (classFields.isEmpty()) {
            throw new IllegalArgumentException("no field is available to compare");
        }
        var field_index = ThreadLocalRandom.current().nextInt(classFields.size());
        sortField = classFields.get(field_index);
        sortField.setAccessible(true);
    }

    /**
     * Compares two objects of the class T by the value of the field that was randomly chosen. It allows null values
     * for the fields, and it treats null value grater than a non-null value (nulls last).
     */
    @Override
    @SneakyThrows
    public int compare(T o1, T o2) {
        var comp1 = (Comparable) sortField.get(o1);
        var comp2 = (Comparable) sortField.get(o2);
        if (comp1 == null && comp2 == null) {
            return 0;
        }
        if (comp1 == null) {
            return 1;
        }
        if (comp2 == null) {
            return -1;
        }

        return comp1.compareTo(comp2);
    }

    /**
     * Returns a statement "Random field comparator of class '%s' is comparing '%s'" where the first param is the name
     * of the type T, and the second parameter is the comparing field name.
     *
     * @return a predefined statement
     */
    @Override
    public String toString() {
        return String.format("Random field comparator of class '%s' is comparing '%s'%n",
                sortField.getDeclaringClass(), sortField.getName());
    }
}
