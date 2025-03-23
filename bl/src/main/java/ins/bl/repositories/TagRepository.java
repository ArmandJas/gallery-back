package ins.bl.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ins.model.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Set<Tag> findAllByNameIn(List<String> names);
}
