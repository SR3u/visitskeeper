package sr3u.showvisitskeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.dto.EntityType;
import sr3u.showvisitskeeper.dto.Query;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;
import sr3u.showvisitskeeper.repo.CompositionRepository;
import sr3u.showvisitskeeper.repo.CompositionTypeRepository;
import sr3u.streamz.streams.Streamex;

import java.util.Optional;

@Component
public class CompositionSearcher implements Searcher {
    @Autowired
    CompositionRepository compositionRepository;

    @Autowired
    CompositionTypeRepository compositionTypeRepository;

    @Override
    public EntityType getEntityType() {
        return EntityType.COMPOSITION;
    }

    @Override
    public Streamex<SearchListEntity> find(Query query) {
        return Streamex.ofCollection(compositionRepository.findByNameContaining(query.getSearchString()))
                .map(this::toEntity);
    }

    private SearchListEntity toEntity(CompositionEntity entity) {
        return SearchListEntity.builder()
                .id(entity.getId())
                .fullName(Optional.ofNullable(getType(entity)).orElse("") + " " + entity.getDisplayName())
                .type(getEntityType().getValue())
                .description("")
                .url("/html/composition?id=" + entity.getId())
                .build();
    }

    private String getType(CompositionEntity entity) {
        if (entity == null) {
            return null;
        }
        return compositionTypeRepository.findById(entity.getType())
                .map(CompositionTypeEntity::getDisplayName).orElse(null);
    }
}
