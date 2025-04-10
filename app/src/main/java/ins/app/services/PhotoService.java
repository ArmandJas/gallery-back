package ins.app.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ins.app.dtos.PhotoPageRequest;
import ins.app.dtos.PhotoPageResponse;
import ins.app.dtos.PhotoPreviewDto;
import ins.app.dtos.PhotoResponse;
import ins.app.dtos.PhotoSaveRequest;
import ins.app.mappers.PhotoMapper;
import ins.bl.repositories.PhotoRepository;
import ins.bl.specifications.PhotoSpecifications;
import ins.model.entities.Photo;
import ins.model.models.PhotoInfo;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public PhotoResponse get(long id) {
        PhotoInfo foundPhoto = photoRepository.findPhotoInfoById(id);

        if (foundPhoto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        return PhotoResponse.to(foundPhoto);
    }

    public byte[] getImage(long id) {
        return photoRepository.findImageById(id).getImage();
    }

    public byte[] getThumbnail(long id) {
        return photoRepository.findThumbnailById(id).getThumbnailImage();
    }

    public PhotoPageResponse search(PhotoPageRequest photoPageRequest) {
        PhotoPageResponse result = new PhotoPageResponse();

        Pageable pageable = PageRequest.of(
                photoPageRequest.getPageNumber(),
                photoPageRequest.getPageSize());

        Specification<Photo> fullSpecification = PhotoSpecifications.createFullSpecification(
                photoPageRequest.toPhotoSearch());

        result.setPhotoTotalCount(photoRepository.count(fullSpecification));
        result.setPhotoPreviews(photoRepository.findAllPreviews(fullSpecification, pageable)
                .stream()
                .map(PhotoPreviewDto::to)
                .toList());

        return result;
    }

    public PhotoResponse save(PhotoSaveRequest photoSaveRequest) {
        Photo entity;
        if (photoSaveRequest.getId() == null) {
            entity = photoMapper.toPhoto(photoSaveRequest);
        } else {
            entity = photoRepository.findPhotoById(photoSaveRequest.getId());
            entity = photoMapper.updatePhotoFields(entity, photoSaveRequest);
        }
        Photo savedPhoto = photoRepository.saveAndFlush(entity);
        return PhotoResponse.to(savedPhoto);
    }

    public void delete(long id) {
        photoRepository.deleteById(id);
    }
}
