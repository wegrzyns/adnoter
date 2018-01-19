package Util;

public class NumberUtil {

    public static boolean between(double i, double minValueInclusive, double maxValueInclusive) {
        return (i >= minValueInclusive && i <= maxValueInclusive);
    }
}
