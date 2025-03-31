package ins.bl.utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThumbnailCreator {
    public static byte[] createThumbnailFrom(byte[] image) {
        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ImageIO.write(
                    Scalr.resize(bufferedImage, Scalr.Mode.FIT_TO_WIDTH, ThumbnailConstants.WIDTH),
                    ThumbnailConstants.FORMAT,
                    outputStream
            );

            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
