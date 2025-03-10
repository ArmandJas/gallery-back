package ins.app.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ins.bl.repositories.TagRepository;
import ins.model.entities.Tag;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Tag getTagByName(String name) {
        return tagRepository.findTagByName(name);
    }
}
