package ins.app.dtos;

import java.time.LocalDateTime;
import java.util.List;

import ins.bl.utilities.PhotoEncoder;
import ins.bl.utilities.TimeUtility;
import ins.bl.utilities.ValidationConstants;
import ins.model.entities.Photo;
import ins.model.entities.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class PhotoDto {

    @NotNull
    private long id;

    @NotBlank
    @Size(max = ValidationConstants.MAX_PHOTO_NAME_LENGTH)
    @Pattern(regexp = ValidationConstants.LETTERS_NUMBERS_SPACES_REGEX)
    private String name;

    @NotBlank
    private String imageBase64;

    @Size(max = ValidationConstants.MAX_PHOTO_DESCRIPTION_LENGTH)
    private String description;

    @NotBlank
    @PastOrPresent
    private String uploadDateTime;

    @Singular
    private List<
            @NotBlank @Size(max = ValidationConstants.MAX_TAG_NAME_LENGTH)
            @Pattern(regexp = ValidationConstants.LETTERS_NUMBERS_SPACES_REGEX)
                    String> tags;

    public static PhotoDto to(Photo photo) {
        if (photo == null) {
            return null;
        }

        LocalDateTime timestamp = photo.getUploadTimestamp();

        return PhotoDto.builder()
                .id(photo.getId())
                .name(photo.getName())
                .imageBase64(PhotoEncoder.encode(photo.getImage()))
                .description(photo.getDescription())
                .uploadDateTime(TimeUtility.formatTimestamp(timestamp))
                .tags(photo.getTags()
                        .stream()
                        .map(Tag::getName)
                        .toList())
                .build();
    }
}
