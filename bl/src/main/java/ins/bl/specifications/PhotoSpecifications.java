package ins.bl.specifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import ins.model.entities.Photo;
import ins.model.entities.Photo_;
import ins.model.entities.Tag;
import ins.model.entities.Tag_;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class PhotoSpecifications {
    public static Specification<Photo> matchesName(String input) {
        String inputNamePattern = '%' + input + '%';

        return (root, query, builder) -> {
            return builder.like(root.get(Photo_.name), inputNamePattern);
        };
    }

    public static Specification<Photo> matchesDescription(String input) {
        String inputDescriptionPattern = '%' + input + '%';

        return (root, query, builder) -> {
            return builder.like(root.get(Photo_.description), inputDescriptionPattern);
        };
    }

    public static Specification<Photo> matchesId(long id) {
        return (root, query, builder) -> {
            return builder.equal(root.get(Photo_.id), id);
        };
    }

    public static Specification<Photo> uploadedBefore(LocalDateTime inputDateTime) {
        return (root, query, builder) -> {
            return builder.lessThan(root.get(Photo_.uploadTimestamp), inputDateTime);
        };
    }

    public static Specification<Photo> uploadedAfter(LocalDateTime inputDateTime) {
        return (root, query, builder) -> {
            return builder.greaterThan(root.get(Photo_.uploadTimestamp), inputDateTime);
        };
    }

    public static Specification<Photo> containsTags(List<String> inputTags) {
        return (root, query, builder) -> {
            Join<Photo, Tag> tagsJoin = root.join(Photo_.tags);
            Predicate containsTagPredicate = tagsJoin.get(Tag_.name).in(inputTags);
            Objects.requireNonNull(query).groupBy(root.get(Photo_.id));

            Expression<Long> tagCount = builder.countDistinct(tagsJoin.get(Tag_.name));
            query.having(builder.equal(tagCount, inputTags.size()));

            return containsTagPredicate;
        };
    }
}
