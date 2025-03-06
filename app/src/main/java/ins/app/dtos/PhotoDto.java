package ins.app.dtos;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import ins.bl.utilities.TimeUtility;
import ins.model.entities.Photo;
import ins.model.entities.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class PhotoDto {
    private long id;
    private String name;
    private String imageBase64;
    private String description;
    private String uploadDateTime;

    @Singular
    private List<String> tags;

    public static PhotoDto to(Photo photo) {
        if (photo == null) {
            return null;
        }

        LocalDateTime timestamp = photo.getUploadTimestamp();

        return PhotoDto.builder()
                .id(photo.getId())
                .name(photo.getName())
                .imageBase64(Base64.getMimeEncoder()
                        .encodeToString(photo.getImage()))
                .description(photo.getDescription())
                .uploadDateTime(TimeUtility.formatTimestamp(timestamp))
                .tags(photo.getTags()
                        .stream()
                        .map(Tag::getName)
                        .toList())
                .build();
    }
}
