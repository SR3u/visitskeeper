package sr3u.showvisitskeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.dto.EntityType;
import sr3u.showvisitskeeper.dto.Query;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.entities.DbEntity;
import sr3u.showvisitskeeper.entities.VenueEntity;
import sr3u.showvisitskeeper.repo.repositories.VenueRepository;
import sr3u.showvisitskeeper.repo.service.VenueRepositoryService;
import sr3u.streamz.streams.Streamex;

import java.util.Comparator;

@Component
public class VenueSearcher implements Searcher {
    @Autowired
    VenueRepositoryService venueRepository;

    @Override
    public EntityType getEntityType() {
        return EntityType.PERSON;
    }

    @Override
    public Streamex<SearchListEntity> find(Query query) {
        return Streamex.ofCollection(venueRepository.findByShortNameContaining(query.getSearchString()))
                .sorted(Comparator.comparing(DbEntity::getDisplayName))
                .map(this::venueToEntity);
    }

    private SearchListEntity venueToEntity(VenueEntity venueEntity) {
        return SearchListEntity.builder()
                .id(venueEntity.getId())
                .type(getEntityType().getValue())
                .fullName(venueEntity.getDisplayName())
                .description(String.valueOf("VENUE"))
                .url("/html/venue?id=" + venueEntity.getId())
                .build();
    }
}
