package PhoneBook;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes person and contains method addPhoneNumber
 * @author Mihail
 * Created on 23.08.2019
 */
public class Person {
    private String name;
    private List<String> phoneNumbers = new ArrayList<>();

    public Person(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumbers.add(phoneNumber);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    void addPhoneNumber(List number) {
        phoneNumbers.addAll(number);
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' +
                ", phoneNumbers=" + phoneNumbers +
                '}';
    }
}
