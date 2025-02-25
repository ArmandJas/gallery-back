package ins.bl.dtos;

import java.util.List;

import ins.bl.utilities.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PhotoUploadDto {
    @NotBlank
    @Size(max = Constants.MAX_PHOTO_NAME_LENGTH)
    @Pattern(regexp = Constants.LETTERS_NUMBERS_SPACES_REGEX)
    private String name;

    @NotBlank
    private String image;

    @Size(max = Constants.MAX_PHOTO_DESCRIPTION_LENGTH)
    private String description;

    private List<
            @NotBlank @Size(max = Constants.MAX_TAG_NAME_LENGTH)
            @Pattern(regexp = Constants.LETTERS_NUMBERS_SPACES_REGEX)
                    String> tags;
}
