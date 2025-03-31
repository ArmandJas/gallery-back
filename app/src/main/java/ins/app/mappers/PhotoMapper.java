package ins.app.mappers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ins.app.dtos.PhotoUpdateRequest;
import ins.app.dtos.PhotoUploadRequest;
import ins.app.services.TagService;
import ins.bl.utilities.ThumbnailCreator;
import ins.bl.utilities.TimeUtility;
import ins.model.entities.Photo;
import ins.model.entities.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhotoMapper {
    private final TagService tagService;

    public Photo toPhoto(PhotoUploadRequest photoUploadRequest) {
        byte[] imageData;
        try {
            imageData = photoUploadRequest.getImage().getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] thumbnailData = ThumbnailCreator.createThumbnailFrom(imageData);

        Photo photo = new Photo();
        photo.setImage(imageData);
        photo.setThumbnailImage(thumbnailData);
        photo.setName(photoUploadRequest.getName().trim());
        photo.setUploadTimestamp(TimeUtility.createTimestamp());

        if (photoUploadRequest.getDescription() != null) {
            photo.setDescription(photoUploadRequest.getDescription().trim());
        }

        List<String> tagList = photoUploadRequest.getTags();
        Map<String, Tag> tagMap = tagService.getTagsByNameList(tagList)
                .stream()
                .collect(Collectors.toMap(
                        Tag::getName,
                        Function.identity()
                ));

        Set<Tag> tags = tagList.stream()
                .map(tagName -> tagMap.computeIfAbsent(tagName, Tag::new))
                .collect(Collectors.toSet());
        photo.setTags(tags);
        return photo;
    }
}
