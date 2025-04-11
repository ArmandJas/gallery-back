package ins.bl.repositories;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import ins.model.entities.Photo;
import ins.model.entities.Photo_;
import ins.model.entities.Tag;
import ins.model.models.PhotoInfo;
import ins.model.models.PhotoPreview;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
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

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, builder);

            if (predicate != null) {
                query.where(predicate);
            }
        }

        Long total = getCount(builder);

        Path<Long> idPath = root.get(Photo_.id);
        Path<String> namePath = root.get(Photo_.name);

        Order order = builder.desc(root.get(Photo_.uploadTimestamp));

        query.multiselect(idPath, namePath)
                .orderBy(order);

        List<Tuple> resultTupleList = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        List<PhotoPreview> resultList = resultTupleList.stream()
                .map(tuple -> PhotoPreview.builder()
                        .id(tuple.get(idPath))
                        .name(tuple.get(namePath))
                        .build())
                .toList();

        return new PageImpl<>(resultList, pageable, total);
    }

    public PhotoInfo findPhotoInfoById(long id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Photo> root = query.from(Photo.class);

        Path<Long> idPath = root.get(Photo_.id);
        Path<String> namePath = root.get(Photo_.name);
        Path<String> descriptionPath = root.get(Photo_.description);
        Path<LocalDateTime> timestampPath = root.get(Photo_.uploadTimestamp);

        Join<Photo, Tag> tagJoin = root.join(Photo_.tags, JoinType.LEFT);

        query.multiselect(idPath, namePath, descriptionPath, timestampPath, tagJoin)
                .where(builder.equal(idPath, id));

        List<Tuple> resultTupleList = em.createQuery(query).getResultList();
        Set<Tag> resultTags = new HashSet<>();

        resultTupleList.forEach(tuple -> {
            Tag tag = tuple.get(tagJoin);
            if (tag != null) {
                resultTags.add(tag);
            }
        });

        Tuple resultTuple = resultTupleList.getFirst();

        return PhotoInfo.builder()
                .id(resultTuple.get(idPath))
                .name(resultTuple.get(namePath))
                .description(resultTuple.get(descriptionPath))
                .uploadTimestamp(resultTuple.get(timestampPath))
                .tags(resultTags)
                .build();
    }

    private Long getCount(CriteriaBuilder builder) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Photo> countRoot = countQuery.from(Photo.class);
        countQuery.select(builder.count(countRoot));

        return em.createQuery(countQuery).getSingleResult();
    }
}
