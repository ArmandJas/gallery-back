package ins.app.services;

import java.util.List;
import java.util.Set;

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

    public Set<Tag> getTagsByNameList(List<String> tagNames) {
        return tagRepository.findAllByNameIn(tagNames);
    }
}
