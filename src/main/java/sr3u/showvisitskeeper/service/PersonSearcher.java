package sr3u.showvisitskeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.dto.EntityType;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.repo.repositories.PersonRepository;
import sr3u.showvisitskeeper.repo.service.PersonRepositoryService;
import sr3u.showvisitskeeper.util.FieldSearcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Component
public class PersonSearcher extends SearcherImpl<PersonRepositoryService, PersonEntity> implements Searcher {
    @Autowired
    PersonRepositoryService personRepository;

    private static final List<FieldSearcher<PersonRepositoryService, PersonEntity>> FIELD_SEARCHERS = Arrays.asList(
            (r,q) -> r.findByShortNameContaining(q.getSearchString()),
            (r,q) -> r.findByFullNameContaining(q.getSearchString())
    );

    @Override
    public EntityType getEntityType() {
        return EntityType.PERSON;
    }


    @Override
    PersonRepositoryService getRepository() {
        return personRepository;
    }

    Collection<FieldSearcher<PersonRepositoryService, PersonEntity>> getFieldSearchers() {
        return FIELD_SEARCHERS;
    }

    Comparator<PersonEntity> entityComparator() {
        return Comparator.comparing(PersonEntity::getDisplayName)
                .thenComparing(PersonEntity::getFullName);
    }

    SearchListEntity itemToEntity(PersonEntity personEntity) {
        return SearchListEntity.builder()
                .id(personEntity.getId())
                .type(getEntityType().getValue())
                .fullName(personEntity.getDisplayName())
                .description(String.valueOf(personEntity.getType()))
                .url("/html/person?id=" + personEntity.getId())
                .build();
    }
}
