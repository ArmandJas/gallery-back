package ins.app.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ins.bl.repositories.TagRepository;
import ins.model.entities.Tag;

@Service
@Transactional
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getTagByName(String name) {
        return tagRepository.findTagByName(name);
    }
}
