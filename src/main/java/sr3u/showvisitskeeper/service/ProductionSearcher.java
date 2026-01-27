package sr3u.showvisitskeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.dto.EntityType;
import sr3u.showvisitskeeper.dto.Query;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;
import sr3u.showvisitskeeper.entities.DbEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.ProductionEntity;
import sr3u.showvisitskeeper.repo.service.CompositionRepositoryService;
import sr3u.showvisitskeeper.repo.service.CompositionTypeRepositoryService;
import sr3u.showvisitskeeper.repo.service.PersonRepositoryService;
import sr3u.showvisitskeeper.repo.service.ProductionRepositoryService;
import sr3u.streamz.streams.Streamex;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductionSearcher implements Searcher {
    @Autowired
    CompositionRepositoryService compositionRepository;

    @Autowired
    ProductionRepositoryService productionRepository;

    @Autowired
    PersonRepositoryService personRepository;

    @Override
    public EntityType getEntityType() {
        return EntityType.COMPOSITION;
    }

    @Override
    public Streamex<SearchListEntity> find(Query query) {
        List<UUID> compositionIds = Streamex.ofCollection(compositionRepository.findByNameContaining(query.getSearchString()))
                .sorted(Comparator.comparing(CompositionEntity::getName))
                .map(ce -> ce.getId())
                .distinct()
                .collect(Collectors.toList());
        return Streamex.ofCollection(productionRepository.findByCompositionIdIn(compositionIds))
                .map(this::toEntity);
    }

    private SearchListEntity toEntity(ProductionEntity entity) {
        return SearchListEntity.builder()
                .id(entity.getId())
                .fullName(getFullName(entity))
                .type(getEntityType().getValue())
                .description("")
                .url("/html/composition?id=" + entity.getId())
                .build();
    }

    private String getFullName(ProductionEntity entity) {
        Optional<CompositionEntity> composition = compositionRepository.findById(entity.getCompositionId());
        String compositionName = composition.map(DbEntity::getDisplayName).orElse("???");
        List<PersonEntity> directors = personRepository.findAllById(entity.getDirectorIds());
        String directorsNames = directors.stream().map(DbEntity::getDisplayName).collect(Collectors.joining(","));
        return compositionName + "(" + directorsNames + ")";
    }

}
