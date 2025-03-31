package ins.app.dtos;

import ins.app.utilities.Base64Encoder;
import ins.model.models.PhotoPreview;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoPreviewDto {
    private long id;
    private String name;
    private String imageBase64;

    public static PhotoPreviewDto to(PhotoPreview input) {
        if (input == null) {
            return null;
        }

        return PhotoPreviewDto.builder()
                .id(input.getId())
                .name(input.getName())
                .imageBase64(Base64Encoder.encodeImage(input.getThumbnailImage()))
                .build();
    }
}
