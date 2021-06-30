package uk.ac.qub.eeecs.gage.util;

public class CountUtil {

    private static int counter;


    public static int setCounter(int num) {
        counter = num;
        return counter;
    }

    public static int getCounter() {
        return counter;
    }
}
