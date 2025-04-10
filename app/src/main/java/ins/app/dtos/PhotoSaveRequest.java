package ins.app.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ins.app.utilities.markers.CreateRequest;
import ins.app.utilities.markers.EditRequest;
import ins.bl.utilities.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PhotoSaveRequest {
    @Null(groups = CreateRequest.class)
    @NotNull(groups = EditRequest.class)
    private Long id;

    @Null(groups = EditRequest.class)
    @NotNull(groups = CreateRequest.class)
    private MultipartFile image;

    @NotBlank
    @Size(max = ValidationConstants.MAX_PHOTO_NAME_LENGTH)
    @Pattern(regexp = ValidationConstants.LETTERS_NUMBERS_SPACES_REGEX)
    private String name;

    @Size(max = ValidationConstants.MAX_PHOTO_DESCRIPTION_LENGTH)
    private String description;

    private List<
            @NotBlank @Size(max = ValidationConstants.MAX_TAG_NAME_LENGTH)
            @Pattern(regexp = ValidationConstants.LETTERS_NUMBERS_SPACES_REGEX)
                    String> tags;
}
