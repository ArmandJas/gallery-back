package ins.bl.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtility {
    public static LocalDateTime createTimestamp() {
        return LocalDateTime.now();
    }

    public static String formatTimestamp(LocalDateTime timestamp) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return df.format(timestamp);
    }
}
