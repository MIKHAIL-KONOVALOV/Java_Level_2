package PhoneBook;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mihail
 * Created on 23.08.2019
 */
public class Main {
    public static void main(String[] args) {
        Person p1 = new Person("Oleg", "345435");
        Person p2 = new Person("Ol", "44543345");
        Person p3 = new Person("Olg", "54543545");
        Person p4 = new Person("Ol", "777777");
        PhoneBook phoneBook = new PhoneBook();

        phoneBook.add(p1);
        phoneBook.add(p2);
        phoneBook.add(p3);
        phoneBook.add(p4);

        phoneBook.display();
        phoneBook.get("Ola");
        phoneBook.get("Ol");
    }
}
