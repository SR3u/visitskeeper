package sr3u.showvisitskeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.dto.EntityType;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;
import sr3u.showvisitskeeper.repo.repositories.CompositionTypeRepository;
import sr3u.showvisitskeeper.repo.service.CompositionTypeRepositoryService;
import sr3u.showvisitskeeper.util.FieldSearcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Component
public class CompositionTypeSearcher extends SearcherImpl<CompositionTypeRepository, CompositionTypeEntity> implements Searcher {
    @Autowired
    CompositionTypeRepository personRepository;

    private static final List<FieldSearcher<CompositionTypeRepository, CompositionTypeEntity>> FIELD_SEARCHERS = Arrays.asList(
            (r,q) -> r.findByValueContaining(q.getSearchString()),
            (r,q) -> r.findByShortNameContaining(q.getSearchString())
    );

    @Override
    public EntityType getEntityType() {
        return EntityType.COMPOSITION_TYPE;
    }


    @Override
    CompositionTypeRepository getRepository() {
        return personRepository;
    }

    Collection<FieldSearcher<CompositionTypeRepository, CompositionTypeEntity>> getFieldSearchers() {
        return FIELD_SEARCHERS;
    }

    Comparator<CompositionTypeEntity> entityComparator() {
        return Comparator.comparing(CompositionTypeEntity::getValue)
                .thenComparing(CompositionTypeEntity::getShortName);
    }

    SearchListEntity itemToEntity(CompositionTypeEntity personEntity) {
        return SearchListEntity.builder()
                .id(personEntity.getId())
                .type(getEntityType().getValue())
                .fullName(personEntity.getDisplayName())
                .description(String.valueOf(personEntity.getDisplayName()))
                .url("/html/person?id=" + personEntity.getId())
                .build();
    }
}

