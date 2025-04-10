package ins.app.dtos;

import java.util.List;
import java.util.Set;

import ins.bl.utilities.TimeUtility;
import ins.model.entities.Tag;
import ins.model.interfaces.PhotoBase;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoResponse {
    private long id;
    private String name;
    private String description;
    private String uploadDateTime;
    private List<String> tags;

    public static PhotoResponse to(PhotoBase photo) {
        if (photo == null) {
            return null;
        }

        return PhotoResponse.builder()
                .id(photo.getId())
                .name(photo.getName())
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
