package ultimate.comparator;

import ultimate.utils.data.Accounts;
import ultimate.utils.model.Account;

import java.util.Comparator;
import java.util.function.Function;

public class TestComparator2 {
    public static void main(String[] args) {
        Comparator<? super Account> r = createComparatorComparing(Account::getBalance);
        var accountWithMaxBalance = Accounts.generateAccountList(10)
                .stream()
//                .max(createComparatorComparing(Account::getBalance))
                .max(composeComparatorThenComparing(r, Account::getBalance))
//                .max(Comparator.comparing(Account::getBalance))
                .orElseThrow();
//        var accountWithMaxBalance = Accounts.generateAccountList(10)
//                .stream()
//                .sorted(Comparator.comparing(Account::getBalance)).toList();

//        accountWithMaxBalance.forEach(a-> System.out.println(a.toString()));
    }

    public static <T, R extends Comparable<? super R>> Comparator<? super T> createComparatorComparing(
            Function<? super T, ? extends R> extractionFunction) {
        return (o1, o2) -> extractionFunction.apply(o1).compareTo(extractionFunction.apply(o2));
    }

    public static <T, R extends Comparable<? super R>> Comparator<? super T> composeComparatorThenComparing(
            Comparator<? super T> c, Function<? super T, ? extends R> extractionFunction) {
        return (o1, o2) -> c.compare(o1, o2) == 0 ?
                extractionFunction.apply(o1).compareTo(extractionFunction.apply(o2)) :
                c.compare(o1, o2);
    }
}
