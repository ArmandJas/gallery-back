package ins.bl.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<PhotoDto> getPhotoPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 12, Sort.by("uploadTimestamp").descending());
        List<PhotoDto> result = new LinkedList<>();
        //TODO: MR3: change to stream
        for (Photo photo : photoRepository.findAll(pageable)) {
            result.add(PhotoDto.to(photo));
        }
        return result;
    }

    public PhotoDto savePhoto(PhotoUploadDto photoUploadDto) {
        Photo newPhoto = photoRepository.save(
                photoMapper.toPhoto(photoUploadDto));
        return PhotoDto.to(newPhoto);
    }
}
