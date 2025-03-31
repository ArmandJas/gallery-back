package ins.app.mappers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import ins.app.dtos.PhotoUpdateRequest;
import ins.app.dtos.PhotoUploadRequest;
import ins.app.services.TagService;
import ins.app.utilities.ThumbnailCreator;
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

        byte[] thumbnailData;
        try {
            thumbnailData = ThumbnailCreator.createThumbnailFrom(imageData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Photo photo = new Photo();
        photo.setImage(imageData);
        photo.setThumbnailImage(thumbnailData);
        photo.setName(photoUploadRequest.getName().trim());

        if (photoUploadRequest.getDescription() != null) {
            photo.setDescription(photoUploadRequest.getDescription().trim());
        }

        photo.setUploadTimestamp(TimeUtility.createTimestamp());

        photo.setTags(createTagSet(photoUploadRequest.getTags()));
        return photo;
    }

    public Photo toPhoto(PhotoUpdateRequest photoUpdateRequest) {
        Photo photo = new Photo();
        photo.setName(photoUpdateRequest.getName().trim());

        if (photoUpdateRequest.getDescription() != null) {
            photo.setDescription(photoUpdateRequest.getDescription().trim());
        }

        photo.setTags(createTagSet(photoUpdateRequest.getTags()));
        return photo;
    }

    private Set<Tag> createTagSet(List<String> tagList) {
        Set<String> tagsToCreate = new HashSet<>(tagList);
        Set<Tag> existingTags = tagService.getTagsByNameList(tagList);

        existingTags.forEach(tag -> {
            if (tagList.contains(tag.getName())) {
                tagsToCreate.remove(tag.getName());
            }
        });

        tagsToCreate.forEach(tag -> existingTags.add(new Tag(tag)));
        return existingTags;
    }
}
