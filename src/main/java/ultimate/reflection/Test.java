package ultimate.reflection;

import ultimate.utils.data.Accounts;
import ultimate.utils.model.Account;

import java.util.Comparator;

public class Test {
    public static void main(String[] args) {
        Comparator<Account> accountComparator = new RandomFieldComparator<>(Account.class);
        System.out.println(accountComparator);
        Accounts.generateAccountList(10)
                .stream()
                .sorted(accountComparator)
                .forEach(System.out::println);
    }
}
