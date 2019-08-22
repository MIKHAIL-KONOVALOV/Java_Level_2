package Duplicate;

import java.util.*;

/**
 * This class test method duplicateSearch
 * @author Mihail
 * Created on 22.08.2019
 */
public class Duplicate {
    public static void main(String[] args) {
        List<Object> objects = Arrays.asList(

//                "kiwi", "orange", "kiwi", "banana", "apricot",
//                "avocado", "pear", "carrot", "cherry", "garlic",
//                "grape", "melon", "leak", "kiwi", "mango",
//                "mushroom", "pear", "olive", "pea", "peanut", "pear",
//                "pepper", "pineapple", "pumpkin", "kiwi"
                  1,2,34,46,347,865,3,6,2,34,46,34,865,865,865,865
        );

        duplicateSearch(objects);
    }

    /**
     * This method search duplicate objects from list
     * and print them to console
     */
    public static void duplicateSearch(List<Object> list) {
        ArrayList<Object> arrayDouble = new ArrayList<Object>();
        for (Object object : list) {
            if (!arrayDouble.contains(object)) {
                int count = Collections.frequency(list, object);
                if (count > 1) {
                    System.out.println("Count of \"" + object + "\" - " + count);
                }
                arrayDouble.add(object); //add duplicate object to array
            }
        }
    }

}
