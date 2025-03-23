package ins.app.utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import ins.bl.utilities.ThumbnailConstants;

public class ThumbnailCreator {
    public static byte[] createThumbnailFrom(byte[] image) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        inputStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(
                Scalr.resize(bufferedImage, Scalr.Mode.FIT_TO_WIDTH, ThumbnailConstants.WIDTH),
                ThumbnailConstants.FORMAT,
                outputStream);

        byte[] result = outputStream.toByteArray();
        outputStream.close();

        return result;
    }
}
