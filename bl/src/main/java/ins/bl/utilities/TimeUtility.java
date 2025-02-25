package ins.bl.utilities;

import java.sql.Timestamp;
import java.text.DateFormat;

public class TimeUtility {
    public static Timestamp createTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String formatTimestamp(Timestamp timestamp) {
        DateFormat df = DateFormat.getDateTimeInstance();
        return df.format(timestamp);
    }
}
