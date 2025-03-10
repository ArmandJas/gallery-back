package ins.app.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<PhotoDto> getPhotoPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 12, Sort.by("uploadTimestamp").descending());
        List<PhotoDto> result = new LinkedList<>();
        //TODO: MR3: change to stream
        for (Photo photo : photoRepository.findAll(pageable)) {
            result.add(PhotoDto.to(photo));
        }
        return result;
    }

    public PhotoDto savePhoto(PhotoUploadRequest photoUploadRequest) {
        Photo entity = photoMapper.toPhoto(photoUploadRequest);
        Photo savedPhoto = photoRepository.saveAndFlush(entity);
        return PhotoDto.to(savedPhoto);
    }
}
