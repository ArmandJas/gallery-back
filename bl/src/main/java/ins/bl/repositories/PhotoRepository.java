package ins.bl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ins.model.entities.Photo;
import ins.model.interfaces.ImageProjection;
import ins.model.interfaces.ThumbnailProjection;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>,
        JpaSpecificationExecutor<Photo>,
        CustomPhotoRepository {
    Photo findPhotoById(Long id);
    ImageProjection findImageById(Long id);
    ThumbnailProjection findThumbnailById(Long id);
}
