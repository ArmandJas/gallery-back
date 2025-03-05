package ins.app.mappers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import ins.app.dtos.PhotoUploadDto;
import ins.app.services.TagService;
import ins.bl.utilities.TimeUtility;
import ins.model.entities.Photo;
import ins.model.entities.Tag;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhotoMapper {
    private final TagService tagService;

    public Photo toPhoto(PhotoUploadDto photoUploadDto) {
        Photo photo = new Photo();
        photo.setName(photoUploadDto.getName().trim());

        if (photoUploadDto.getDescription() != null) {
            photo.setDescription(photoUploadDto.getDescription().trim());
        }

        photo.setUploadTimestamp(TimeUtility.createTimestamp());

        try {
            photo.setImage(photoUploadDto.getImage().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Sets tags with just names, no ids
        photo.setTags(tagSetBuilder(photoUploadDto.getTags()));

        Set<Tag> newTags = new HashSet<>();
        // Gets full tags from database by name
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

    // Creates a set of tags from a string list of tag names
    private static Set<Tag> tagSetBuilder(List<String> input) {
        Set<Tag> tagSet = new HashSet<Tag>();
        for (String tag : input) {
            Tag newTag = new Tag(tag.trim());
            tagSet.add(newTag);
        }
        return tagSet;
    }
}
