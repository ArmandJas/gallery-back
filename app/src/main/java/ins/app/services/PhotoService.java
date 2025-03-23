package ins.app.services;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ins.app.dtos.PhotoDto;
import ins.app.dtos.PhotoPageRequest;
import ins.app.dtos.PhotoPageResponse;
import ins.app.dtos.PhotoPreviewResponse;
import ins.app.dtos.PhotoUploadRequest;
import ins.app.mappers.PhotoMapper;
import ins.bl.repositories.PhotoRepository;
import ins.bl.specifications.PhotoSpecifications;
import ins.model.entities.Photo;
import ins.model.entities.Photo_;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public PhotoDto getPhoto(long id) {
        Photo foundPhoto = photoRepository.findPhotoById(id);
        if (foundPhoto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        return PhotoDto.to(foundPhoto);
    }

    public PhotoPageResponse getPhotoPage(PhotoPageRequest photoPageRequest, int pageNumber) {
        PhotoPageResponse result = new PhotoPageResponse();

        Pageable pageable = PageRequest.of(pageNumber,
                photoPageRequest.getPageSize(),
                Sort.by(Photo_.UPLOAD_TIMESTAMP).descending());

        Specification<Photo> fullSpecification = createSpecificationFromRequest(photoPageRequest);

        result.setPhotoCount(photoRepository.count(fullSpecification));
        result.setPhotoPreviews(photoRepository.findAllPreviews(fullSpecification, pageable)
                .stream()
                .map(PhotoPreviewResponse::to)
                .toList());

        return result;
    }

    public PhotoDto savePhoto(PhotoUploadRequest photoUploadRequest) {
        Photo entity = photoMapper.toPhoto(photoUploadRequest);
        Photo savedPhoto = photoRepository.saveAndFlush(entity);
        return PhotoDto.to(savedPhoto);
    }

    private static LocalDateTime parseDateTime(String inputDate, boolean endOfDay) {
        if (inputDate == null || inputDate.isEmpty()) {
            return null;
        }
        final String TIME_APPEND_DAY_START = "T00:00:00";
        final String TIME_APPEND_DAY_END = "T23:59:59";

        if (endOfDay) {
            inputDate += TIME_APPEND_DAY_END;
        } else {
            inputDate += TIME_APPEND_DAY_START;
        }
        return LocalDateTime.parse(inputDate);
    }

    private static Specification<Photo> createSpecificationFromRequest(PhotoPageRequest photoPageRequest) {
        Specification<Photo> specification = Specification.where(null);

        if (photoPageRequest.getId() != 0) {
            specification = specification
                    .and(PhotoSpecifications.matchesId(photoPageRequest.getId()));
        }
        if (photoPageRequest.getName() != null) {
            specification = specification
                    .and(PhotoSpecifications.matchesName(photoPageRequest.getName()));
        }
        if (photoPageRequest.getDescription() != null) {
            specification = specification
                    .and(PhotoSpecifications.matchesDescription(photoPageRequest.getDescription()));
        }
        if (photoPageRequest.getUploadDateTimeStart() != null) {
            specification = specification
                    .and(PhotoSpecifications.uploadedAfter(
                            parseDateTime(photoPageRequest.getUploadDateTimeStart(), false)));
        }
        if (photoPageRequest.getUploadDateTimeEnd() != null) {
            specification = specification
                    .and(PhotoSpecifications.uploadedBefore(
                            parseDateTime(photoPageRequest.getUploadDateTimeEnd(), true)));
        }
        if (photoPageRequest.getTags() != null) {
            specification = specification
                    .and(PhotoSpecifications.containsTags(
                            Arrays.stream(photoPageRequest.getTags()).toList()));
        }
        return specification;
    }
}
