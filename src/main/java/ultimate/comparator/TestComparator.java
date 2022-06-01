package ultimate.comparator;

import ultimate.utils.data.Accounts;
import ultimate.utils.model.Account;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.Function;

public class TestComparator {

    public static void main(String[] args) {
        var accountWithMaxBalance = Accounts.generateAccountList(10)
                .stream()
                .max(createComparatorComparing(Account::getBalance))
//                .max(Comparator.comparing(Account::getBalance))
                .orElseThrow();
    }

//    private static final Comparator<ScriptEngineFactory> COMPARATOR = Comparator.comparing(
//            ScriptEngineFactory::getEngineName,
//            Comparator.nullsLast(Comparator.naturalOrder())
//    );

    static <T, R extends Comparable<? super R>> Comparator<T> createComparatorComparing(Function<? super T, ? extends R> extractionFunction) {
        return (Comparator<T> & Serializable)
                (o1, o2) -> extractionFunction.apply(o1).compareTo(extractionFunction.apply(o2));
    }

//    static <T, R extends Comparable<? super R>> Comparator<T> createComparatorComparing(Function<? extends T, R> keyExtractor) {
//        return null;
////        return (o1, o2) -> keyExtractor.apply(o1).compareTo(o2);
//    }


}
