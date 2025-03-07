package ins.app.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ins.app.dtos.PhotoDto;
import ins.app.dtos.PhotoUploadRequest;
import ins.app.mappers.PhotoMapper;
import ins.bl.repositories.PhotoRepository;
import ins.model.entities.Photo;
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

    public PhotoDto savePhoto(PhotoUploadRequest photoUploadRequest) {
        Photo entity = photoMapper.toPhoto(photoUploadRequest);
        Photo savedPhoto = photoRepository.saveAndFlush(entity);
        return PhotoDto.to(savedPhoto);
    }
}
