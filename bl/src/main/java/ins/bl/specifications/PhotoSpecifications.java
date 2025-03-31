package ins.bl.specifications;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import ins.bl.utilities.TimeUtility;
import ins.model.entities.Photo;
import ins.model.entities.Photo_;
import ins.model.entities.Tag;
import ins.model.entities.Tag_;
import ins.model.models.PhotoSearch;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class PhotoSpecifications {
    private static String makeLikePattern(String input) {
        return '%' + input + '%';
    }

    private static boolean isNotNullOrEmpty(String input) {
        return input != null && !input.isEmpty();
    }

    public static Specification<Photo> matchesName(String input) {
        return (root, query, builder) ->
                builder.like(root.get(Photo_.name), makeLikePattern(input));
    }

    public static Specification<Photo> matchesDescription(String input) {
        return (root, query, builder) ->
                builder.like(root.get(Photo_.description), makeLikePattern(input));
    }

    public static Specification<Photo> matchesId(long id) {
        return (root, query, builder) ->
                builder.equal(root.get(Photo_.id), id);
    }

    public static Specification<Photo> uploadedBefore(LocalDateTime inputDateTime) {
        return (root, query, builder) ->
                builder.lessThan(root.get(Photo_.uploadTimestamp), inputDateTime);
    }

    public static Specification<Photo> uploadedAfter(LocalDateTime inputDateTime) {
        return (root, query, builder) ->
                builder.greaterThan(root.get(Photo_.uploadTimestamp), inputDateTime);
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

    public static Specification<Photo> createFullSpecification(PhotoSearch photoSearch) {
        Specification<Photo> specification = Specification.where(null);

        if (photoSearch.getId() != 0) {
            specification = specification
                    .and(matchesId(photoSearch.getId()));
        }
        if (isNotNullOrEmpty(photoSearch.getName())) {
            specification = specification
                    .and(matchesName(photoSearch.getName()));
        }
        if (isNotNullOrEmpty(photoSearch.getDescription())) {
            specification = specification
                    .and(matchesDescription(photoSearch.getDescription()));
        }
        if (photoSearch.getUploadDateStart() != null) {
            LocalDateTime startDateTime = TimeUtility.parseDateTime(photoSearch.getUploadDateStart(), false);
            specification = specification
                    .and(uploadedAfter(startDateTime));
        }
        if (photoSearch.getUploadDateEnd() != null) {
            LocalDateTime endDateTime = TimeUtility.parseDateTime(photoSearch.getUploadDateEnd(), true);
            specification = specification
                    .and(uploadedBefore(endDateTime));
        }
        if (photoSearch.getTags() != null && photoSearch.getTags().length != 0) {
            List<String> tagList = Arrays.stream(photoSearch.getTags()).toList();
            specification = specification
                    .and(containsTags(tagList));
        }
        return specification;
    }
}
