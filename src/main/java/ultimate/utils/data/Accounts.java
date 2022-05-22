package ultimate.utils.data;


import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import ultimate.utils.model.Account;
import ultimate.utils.model.Sex;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Accounts {
    public static int MAX_BALANCE_VALUE = 200_000;

    public static Account generateAccount() {
        Person person = generatePerson();
        Account account = convertToAccount(person);
        fillCommonRandomFields(account);
        return account;
    }

    public static List<Account> generateAccountList(int size) {
        return Stream.generate(Accounts::generateAccount)
                .limit(size)
                .collect(toList());
    }

    private static Person generatePerson() {
        Fairy fairy = Fairy.create();
        return fairy.person();
    }

    private static Account convertToAccount(Person person) {
        Account account = new Account();
        fillAccount(account, person);
        return account;
    }


    private static void fillAccount(Account account, Person person) {
        account.setFirstName(person.getFirstName());
        account.setLastName(person.getLastName());
        account.setEmail(person.getEmail());
        account.setBirthday(LocalDate.of(
                person.getDateOfBirth().getYear(),
                person.getDateOfBirth().getMonth(),
                person.getDateOfBirth().getDayOfMonth()));
        account.setSex(Sex.valueOf(person.getSex().name()));
    }

    private static void fillCommonRandomFields(Account account) {
        BigDecimal balance = randomBigDecimal(MAX_BALANCE_VALUE);
        account.setBalance(balance);
        account.setCreationDate(LocalDate.now());
    }

    private static BigDecimal randomBigDecimal(int max) {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(max));
    }
}

