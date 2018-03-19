package cea.Util;

import java.util.List;

public class NumberUtil {

    public static boolean between(double i, double minValueInclusive, double maxValueInclusive) {
        return (i >= minValueInclusive && i <= maxValueInclusive);
    }

    public static double average(List<Integer> integers) {
        return integers.stream().mapToDouble(Integer::intValue).average().getAsDouble();
    }

    public static double meanAbsoluteDevation(List<Integer> integers) {
        double median = average(integers);

        return integers.stream().mapToDouble(Integer::intValue)
                .map(d -> Math.abs(d - median))
                .average().getAsDouble();
    }
}
