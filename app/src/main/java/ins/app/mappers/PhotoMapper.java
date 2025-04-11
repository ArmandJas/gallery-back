package ins.app.mappers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ins.app.dtos.PhotoSaveRequest;
import ins.app.services.TagService;
import ins.bl.utilities.ThumbnailCreator;
import ins.bl.utilities.TimeUtility;
import ins.model.entities.Photo;
import ins.model.entities.Tag;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhotoMapper {
    private final TagService tagService;

    public Photo toPhoto(PhotoSaveRequest photoSaveRequest) {
        byte[] imageData;
        try {
            imageData = photoSaveRequest.getImage().getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] thumbnailData = ThumbnailCreator.createThumbnailFrom(imageData);

        Photo photo = new Photo();
        photo.setImage(imageData);
        photo.setThumbnailImage(thumbnailData);
        photo.setName(photoSaveRequest.getName().trim());
        photo.setUploadTimestamp(TimeUtility.createTimestamp());

        if (photoSaveRequest.getDescription() != null) {
            photo.setDescription(photoSaveRequest.getDescription().trim());
        }

        List<String> tagList = photoSaveRequest.getTags();
        photo.setTags(createInstantiatedTagSet(tagList));
        return photo;
    }

    public Photo updatePhotoFields(Photo photo, PhotoSaveRequest photoSaveRequest) {
        photo.setName(photoSaveRequest.getName().trim());

        if (photoSaveRequest.getDescription() != null) {
            photo.setDescription(photoSaveRequest.getDescription().trim());
        }

        List<String> tagList = photoSaveRequest.getTags();
        photo.setTags(createInstantiatedTagSet(tagList));
        return photo;
    }

    private Set<Tag> createInstantiatedTagSet(List<String> tagList) {
        Map<String, Tag> tagMap = tagService.getTagsByNameList(tagList)
                .stream()
                .collect(Collectors.toMap(
                        Tag::getName,
                        Function.identity()
                ));

        return tagList.stream()
                .map(tagName -> tagMap.computeIfAbsent(tagName, Tag::new))
                .collect(Collectors.toSet());
    }
}
