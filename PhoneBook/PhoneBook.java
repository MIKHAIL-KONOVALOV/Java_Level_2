package PhoneBook;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes PhoneBook and contains methods
 * add, get, display.
 * @author Mihail
 * Created on 23.08.2019
 */
public class PhoneBook {
    private int size;
    private List<Person> people = new ArrayList<Person>();

    int getSize() {
        return size;
    }

    boolean isEmpty(){
        return size == 0;
    }

    void add(Person p) {
        if (isEmpty()) {
            people.add(p);
            size++;
        } else {
            boolean find = false;
            for (Person person : people) {
                if (p.getName() == person.getName()) {
                    person.addPhoneNumber(p.getPhoneNumbers());
                    find = true;
                    break;
                }
            }
            if (!find) {
                people.add(p);
                size++;
            }
        }
    }

    void get(String name) {
        for (Person person : people) {
            if (person.getName() == name) {
                System.out.println(person);
                return;
            }
        }
        System.out.println("Person with name "+ name + " doesn't exist.");
    }

    void display() {
        for (Person person : people) {
            System.out.println(person);
        }
    }
}
