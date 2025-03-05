package ins.bl.utilities;

import java.util.Base64;

public class PhotoEncoder {
    public static String encode(byte[] input) {
        String result = Base64.getMimeEncoder().encodeToString(input);

        // Add base64 annotation and remove padding
        result = "data:image;base64," + result;
        result = result.replaceAll("(\\s|=+)", "");
        return result;
    }

    public static byte[] decode(String input) {
        input = input.split("=")[0];
        return Base64.getMimeDecoder().decode(input);
    }
}
