package ins.app.dtos;

import java.time.LocalDate;

import ins.model.models.PhotoSearch;
import lombok.Data;

@Data
public class PhotoPageRequest {
    private long id;
    private int pageNumber;
    private int pageSize;
    private String name;
    private String description;
    private LocalDate uploadDateStart;
    private LocalDate uploadDateEnd;
    private String[] tags;

    public PhotoSearch toPhotoSearch() {
        return PhotoSearch.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .uploadDateStart(getUploadDateStart())
                .uploadDateEnd(getUploadDateEnd())
                .tags(getTags())
                .build();
    }
}
