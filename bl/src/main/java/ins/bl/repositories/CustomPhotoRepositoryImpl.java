package ins.bl.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import ins.model.entities.Photo;
import ins.model.entities.Photo_;
import ins.model.models.PhotoPreview;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomPhotoRepositoryImpl implements CustomPhotoRepository {
    private final EntityManager em;

    public Page<PhotoPreview> findAllPreviews(Specification<Photo> spec, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Photo> root = query.from(Photo.class);

        query.multiselect(root.get(Photo_.id), root.get(Photo_.name), root.get(Photo_.thumbnailImage))
                .orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, builder);

            if (predicate != null) {
                query.where(predicate);
            }
        }

        List<Tuple> resultTupleList = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Photo> countRoot = countQuery.from(Photo.class);
        countQuery.select(builder.count(countRoot));
        Long total = em.createQuery(countQuery).getSingleResult();

        List<PhotoPreview> resultList = resultTupleList.stream()
                .map(PhotoPreview::to)
                .toList();

        return new PageImpl<>(resultList, pageable, total);
    }
}
