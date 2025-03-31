package ins.bl.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import ins.model.entities.Photo;
import ins.model.models.PhotoPreview;

@Repository
public interface CustomPhotoRepository {
    Page<PhotoPreview> findAllPreviews(Specification<Photo> spec, Pageable pageable);
}
