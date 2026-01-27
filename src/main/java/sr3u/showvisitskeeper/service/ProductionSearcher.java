package sr3u.showvisitskeeper.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.dto.EntityType;
import sr3u.showvisitskeeper.dto.Query;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.DbEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.ProductionEntity;
import sr3u.showvisitskeeper.repo.service.CompositionRepositoryService;
import sr3u.showvisitskeeper.repo.service.PersonRepositoryService;
import sr3u.showvisitskeeper.repo.service.ProductionRepositoryService;
import sr3u.streamz.streams.Streamex;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
        Map<UUID, CompositionEntity> compositionsMap = Streamex.ofCollection(compositionRepository.findByNameContaining(query.getSearchString()))
                .stream()
                .distinct()
                .map(ce -> Pair.of(ce.getId(), ce))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (a, b) -> b));
        Collection<UUID> compositionIds = compositionsMap.keySet();
        return Streamex.ofCollection(productionRepository.findByCompositionIdIn(compositionIds))
                .map(pe->toEntity(pe, compositionsMap));
    }

    private SearchListEntity toEntity(ProductionEntity entity, Map<UUID, CompositionEntity> compositionsMap) {
        return SearchListEntity.builder()
                .id(entity.getId())
                .fullName(getFullName(entity, compositionsMap))
                .type(getEntityType().getValue())
                .description("")
                .url("/html/composition?id=" + entity.getId())
                .build();
    }

    private String getFullName(ProductionEntity entity, Map<UUID, CompositionEntity> compositionsMap) {
        Optional<CompositionEntity> composition = Optional.ofNullable(compositionsMap.get(entity.getCompositionId()));
        String compositionName = composition.map(DbEntity::getDisplayName).orElse("???");
        List<PersonEntity> directors = personRepository.findAllById(entity.getDirectorIds());
        String directorsNames = directors.stream().map(DbEntity::getDisplayName).collect(Collectors.joining(","));
        return compositionName + "(" + directorsNames + ")";
    }

}
