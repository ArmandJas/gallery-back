package ins.model.models;

import jakarta.persistence.Tuple;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoPreview {
    private long id;
    private String name;
    private byte[] thumbnailImage;

    public static PhotoPreview to(Tuple input) {
        if (input == null) {
            return null;
        }
        return PhotoPreview.builder()
                .id(input.get(0, long.class))
                .name(input.get(1, String.class))
                .thumbnailImage((input.get(2, byte[].class)))
                .build();
    }
}
