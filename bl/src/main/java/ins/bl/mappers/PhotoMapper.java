package ins.bl.mappers;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import ins.bl.dtos.PhotoDto;
import ins.bl.dtos.PhotoUploadDto;
import ins.bl.services.TagService;
import ins.bl.utilities.PhotoEncoder;
import ins.bl.utilities.TimeUtility;
import ins.model.entities.Photo;
import ins.model.entities.Tag;

@Component
public class PhotoMapper {
    private final TagService tagService;

    public PhotoMapper(TagService tagService) {
        this.tagService = tagService;
    }

    public static Photo toPhoto(PhotoDto photoDto) {
        Photo photo = new Photo();
        photo.setId(photoDto.getId());
        photo.setName(photoDto.getName());
        photo.setDescription(photoDto.getDescription());

        //WARN: unused/untested
        photo.setUploadTimestamp(Timestamp.valueOf(
                photoDto.getUploadDateTime()));

        String image = photoDto.getImage().split("=")[0];
        photo.setImage(PhotoEncoder.decode(image));
        return photo;
    }

    public Photo toPhoto(PhotoUploadDto photoUploadDto) {
        Photo photo = new Photo();
        photo.setName(photoUploadDto.getName().trim());
        photo.setDescription(photoUploadDto.getDescription().trim());
        photo.setUploadTimestamp(TimeUtility.createTimestamp());

        String image = photoUploadDto.getImage().split("=")[0];
        photo.setImage(PhotoEncoder.decode(image));

        // Sets tags with just names, no ids
        photo.setTags(tagSetBuilder(photoUploadDto.getTags()));

        Set<Tag> newTags = new HashSet<>();
        // Gets full tags from database by name
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
