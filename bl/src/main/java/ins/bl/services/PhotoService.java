package ins.bl.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ins.bl.dtos.PhotoDto;
import ins.bl.dtos.PhotoUploadDto;
import ins.bl.mappers.PhotoMapper;
import ins.bl.repositories.PhotoRepository;
import ins.model.entities.Photo;

//transactional?
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
        PhotoDto result = PhotoDto.to(photoRepository.findPhotoById(id));
        if (result != null) {
            return result;
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }

    public PhotoDto savePhoto(PhotoUploadDto photoUploadDto) {
        Photo newPhoto = photoRepository.save(
                photoMapper.toPhoto(photoUploadDto));
        return PhotoDto.to(newPhoto);
    }
}
