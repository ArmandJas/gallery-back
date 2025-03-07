package ins.bl.utilities;

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
}
