package ins.app.dtos;

import java.util.List;
import java.util.Set;

import ins.app.utilities.Base64Encoder;
import ins.bl.utilities.TimeUtility;
import ins.model.entities.Photo;
import ins.model.entities.Tag;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoDto {
    private long id;
    private String name;
    private String imageBase64;
    private String description;
    private String uploadDateTime;
    private List<String> tags;

    public static PhotoDto to(Photo photo) {
        if (photo == null) {
            return null;
        }

        return PhotoDto.builder()
                .id(photo.getId())
                .name(photo.getName())
                .imageBase64(Base64Encoder.encodeImage(photo.getImage()))
                .description(photo.getDescription())
                .uploadDateTime(TimeUtility.formatTimestamp(photo.getUploadTimestamp()))
                .tags(buildStringTagList(photo.getTags()))
                .build();
    }

    private static List<String> buildStringTagList(Set<Tag> tags) {
        return tags.stream()
                .map(Tag::getName)
                .toList();
    }
}
