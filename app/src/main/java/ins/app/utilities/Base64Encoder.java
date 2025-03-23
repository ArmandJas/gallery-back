package ins.app.utilities;

import java.util.Base64;

public class Base64Encoder {
    public static String encodeImage(byte[] image) {
        return Base64.getMimeEncoder().encodeToString(image);
    }
}
