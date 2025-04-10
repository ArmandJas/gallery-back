package ins.model.interfaces;

import java.time.LocalDateTime;
import java.util.Set;

import ins.model.entities.Tag;

public interface PhotoBase {
    Long getId();
    String getName();
    String getDescription();
    LocalDateTime getUploadTimestamp();
    Set<Tag> getTags();
}
