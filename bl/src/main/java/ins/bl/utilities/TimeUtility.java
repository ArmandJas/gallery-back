package ins.bl.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtility {
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime createTimestamp() {
        return LocalDateTime.now();
    }

    public static String formatTimestamp(LocalDateTime timestamp) {
        return df.format(timestamp);
    }

    public static LocalDateTime parseDateTime(LocalDate inputDate, boolean endOfDay) {
        if (endOfDay) {
            return inputDate.atStartOfDay().plusDays(1).minusNanos(1);
        }

        return inputDate.atStartOfDay();
    }
}
