package ins.app.dtos;

import java.util.List;

import lombok.Data;

@Data
public class PhotoPageResponse {
    private List<PhotoPreviewResponse> photoPreviews;
    private long photoCount;
}
