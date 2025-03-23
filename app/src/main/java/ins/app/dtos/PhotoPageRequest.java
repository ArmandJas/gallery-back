package ins.app.dtos;

import lombok.Data;

@Data
public class PhotoPageRequest {
    private long id;
    private int pageSize;
    private String name;
    private String description;
    private String uploadDateTimeStart;
    private String uploadDateTimeEnd;
    private String[] tags;
}
