package ins.app.dtos;

import java.time.LocalDate;

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
}
