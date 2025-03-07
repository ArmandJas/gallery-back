package ins.app.mappers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import ins.app.dtos.PhotoUploadRequest;
import ins.app.services.TagService;
import ins.bl.utilities.TimeUtility;
import ins.model.entities.Photo;
import ins.model.entities.Tag;
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

        Photo photo = new Photo();
        photo.setImage(imageData);
        photo.setName(photoUploadRequest.getName().trim());

        if (photoUploadRequest.getDescription() != null) {
            photo.setDescription(photoUploadRequest.getDescription().trim());
        }

        photo.setUploadTimestamp(TimeUtility.createTimestamp());

        photo.setTags(buildTagSet(photoUploadRequest.getTags()));

        Set<Tag> newTags = new HashSet<>();

        // TODO: MR3: FIX MULTIPLE CALLS TO DB, REFACTOR
        for (Tag tag : photo.getTags()) {
            Tag found = tagService.getTagByName(tag.getName());
            if (found == null) {
                found = new Tag(tag.getName());
            }
            newTags.add(found);
        }
        photo.setTags(newTags);
        return photo;
    }

    private static Set<Tag> buildTagSet(List<String> input) {
        Set<Tag> tagSet = new HashSet<>();
        for (String tag : input) {
            Tag newTag = new Tag(tag.trim());
            tagSet.add(newTag);
        }
        return tagSet;
    }
}
