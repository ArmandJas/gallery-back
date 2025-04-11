package ins.model.models;

import java.time.LocalDateTime;
import java.util.Set;

import ins.model.entities.Tag;
import ins.model.interfaces.PhotoBase;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoInfo implements PhotoBase {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime uploadTimestamp;
    private Set<Tag> tags;
}
