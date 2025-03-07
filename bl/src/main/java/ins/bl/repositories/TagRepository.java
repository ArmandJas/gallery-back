package ins.bl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ins.model.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findTagByName(String name);
}
