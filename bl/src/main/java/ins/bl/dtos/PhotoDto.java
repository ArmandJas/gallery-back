package ins.bl.dtos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ins.bl.utilities.Constants;
import ins.bl.utilities.PhotoEncoder;
import ins.bl.utilities.TimeUtility;
import ins.model.entities.Photo;
import ins.model.entities.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PhotoDto {

    @NotNull
    private long id;

    @NotBlank
    @Size(max = Constants.MAX_PHOTO_NAME_LENGTH)
    @Pattern(regexp = Constants.LETTERS_NUMBERS_SPACES_REGEX)
    private String name;

    @NotBlank
    private String image;

    @Size(max = Constants.MAX_PHOTO_DESCRIPTION_LENGTH)
    private String description;

    @NotBlank
    @PastOrPresent
    private String uploadDateTime;

    private List<
            @NotBlank @Size(max = Constants.MAX_TAG_NAME_LENGTH)
            @Pattern(regexp = Constants.LETTERS_NUMBERS_SPACES_REGEX)
                    String> tags;

    public static PhotoDto to(Photo photo) {
        if (photo == null) {
            return null;
        }
        PhotoDto dto = new PhotoDto();
        dto.setId(photo.getId());
        dto.setName(photo.getName());
        dto.setImage(PhotoEncoder.encode(photo.getImage()));
        dto.setDescription(photo.getDescription());

        Timestamp ts = photo.getUploadTimestamp();
        dto.setUploadDateTime(TimeUtility.formatTimestamp(ts));

        dto.setTags(new ArrayList<>());
        for (Tag tag : photo.getTags()) {
            dto.tags.add(tag.getName());
        }
        return dto;
    }
}
