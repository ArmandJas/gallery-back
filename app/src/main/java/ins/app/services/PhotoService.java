package ins.app.services;

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
import ins.app.dtos.PhotoPreviewDto;
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

    public PhotoPageResponse search(PhotoPageRequest photoPageRequest) {
        PhotoPageResponse result = new PhotoPageResponse();

        Pageable pageable = PageRequest.of(
                photoPageRequest.getPageNumber(),
                photoPageRequest.getPageSize(),
                Sort.by(Photo_.uploadTimestamp.getName()).descending());

        Specification<Photo> fullSpecification = PhotoSpecifications.createFullSpecification(
                photoPageRequest.toPhotoSearch());

        result.setPhotoCount(photoRepository.count(fullSpecification));
        result.setPhotoPreviews(photoRepository.findAllPreviews(fullSpecification, pageable)
                .stream()
                .map(PhotoPreviewDto::to)
                .toList());

        return result;
    }

    public PhotoDto saveNew(PhotoUploadRequest photoUploadRequest) {
        Photo entity = photoMapper.toPhoto(photoUploadRequest);
        Photo savedPhoto = photoRepository.saveAndFlush(entity);
        return PhotoDto.to(savedPhoto);
    }
}
