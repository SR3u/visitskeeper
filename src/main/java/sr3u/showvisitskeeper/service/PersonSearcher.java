package sr3u.showvisitskeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.dto.EntityType;
import sr3u.showvisitskeeper.dto.Query;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.repo.PersonRepository;
import sr3u.streamz.streams.Streamex;

@Component
public class PersonSearcher implements Searcher {
    @Autowired
    PersonRepository personRepository;

    @Override
    public EntityType getEntityType() {
        return EntityType.PERSON;
    }

    @Override
    public Streamex<SearchListEntity> find(Query query) {
        return Streamex.ofCollection(personRepository.findByShortNameContaining(query.getSearchString()))
                .map(this::personToEntity);
    }

    private SearchListEntity personToEntity(PersonEntity personEntity) {
        return SearchListEntity.builder()
                .id(personEntity.getId())
                .type(getEntityType().getValue())
                .fullName(personEntity.getDisplayName())
                .description(String.valueOf(personEntity.getType()))
                .url("/html/person?id=" + personEntity.getId())
                .build();
    }
}
