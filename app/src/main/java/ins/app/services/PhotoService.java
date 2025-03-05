package ins.app.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ins.app.dtos.PhotoDto;
import ins.app.dtos.PhotoUploadDto;
import ins.app.mappers.PhotoMapper;
import ins.bl.repositories.PhotoRepository;
import ins.model.entities.Photo;

@Service
@Transactional
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public PhotoService(PhotoRepository photoRepository, PhotoMapper photoMapper) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
    }

    public PhotoDto getPhoto(long id) {
        PhotoDto photoDto = PhotoDto.to(photoRepository.findPhotoById(id));
        if (photoDto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        return PhotoDto.to(photoRepository.findPhotoById(id));
    }

    public PhotoDto savePhoto(PhotoUploadDto photoUploadDto) {
        Photo entity = photoMapper.toPhoto(photoUploadDto);
        Photo savedPhoto = photoRepository.saveAndFlush(entity);
        return PhotoDto.to(savedPhoto);
    }
}
