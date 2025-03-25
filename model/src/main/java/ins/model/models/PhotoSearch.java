package ins.model.models;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoSearch {
    private long id;
    private String name;
    private String description;
    private LocalDate uploadDateStart;
    private LocalDate uploadDateEnd;
    private String[] tags;
}
