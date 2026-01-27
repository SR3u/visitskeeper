package sr3u.showvisitskeeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.PagedCollection;
import sr3u.showvisitskeeper.dto.PagesInfo;
import sr3u.showvisitskeeper.dto.smart.Production;
import sr3u.showvisitskeeper.dto.smart.annotations.Mapper;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.ProductionEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;
import sr3u.showvisitskeeper.exceptions.NotFoundException;
import sr3u.showvisitskeeper.repo.service.CompositionRepositoryService;
import sr3u.showvisitskeeper.repo.service.ProductionRepositoryService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static sr3u.showvisitskeeper.service.SearchService.getPagesCount;

@Service
@RequiredArgsConstructor
public class ProductionService {
    private final ProductionRepositoryService productionRepository;
    private final CompositionRepositoryService compositionRepository;
    private final VisitService visitService;
    private final Mapper mapper;

    public Production productionInfo(UUID id) {
        return RepositoryHolder.INSTANCE.getMapper().toProduction(productionRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    public PagedCollection<Production> find(UUID id, UUID venueId, UUID directorId, UUID conductorId, UUID composerId,
                                            UUID visitId, UUID artistId, UUID attendeeId, UUID compositionTypeId,
                                            UUID compositionId, long pageSize, long page) {
        List<ProductionEntity> res = new ArrayList<>();
        if (id != null) {
            res.add(productionInfo(id));
        }
        if (directorId != null) {
            res.addAll(productionRepository.findByDirectorIdsIn(Collections.singleton(directorId)));
        }
        List<UUID> compositionIds = new ArrayList<>();
        if (compositionId != null) {
            compositionIds.add(compositionId);
        }
        if (composerId != null) {
            compositionIds.addAll(compositionRepository.findByComposerIds(composerId).stream().map(CompositionEntity::getId).toList());
        }
        if (compositionTypeId != null) {
            compositionIds.addAll(compositionRepository.findByTypeId(compositionTypeId).stream().map(CompositionEntity::getId).toList());
        }
        if (!compositionIds.isEmpty()) {
            res.addAll(productionRepository.findByCompositionIdIn(compositionIds).stream().toList());
        }
        List<UUID> productionIds = visitService.find(visitId, venueId, null, conductorId,
                        null, null, artistId, attendeeId, null,
                        null,
                        -1, -1)
                .getContent().stream()
                .map(VisitEntity::getProductionIds)
                .flatMap(Collection::stream)
                .distinct()
                .toList();
        res.addAll(productionRepository.findAllById(productionIds));
        return paging(res.stream()
                .distinct()
                .map(mapper::toProduction)
                .distinct()
                .sorted(Comparator.comparing(Production::getDisplayName))
                .toList(), pageSize, page);
    }

    public static <T> PagedCollection<T> paging(Collection<T> allItems, long pageSize, long page) {
        if (page < 0 && pageSize <= 0) {
            return new PagedCollection<>(PagesInfo.builder()
                    .pages(1)
                    .items(BigDecimal.valueOf(allItems.size()))
                    .build(), allItems);
        }
        return new PagedCollection<>(PagesInfo.builder()
                .pages(getPagesCount(pageSize, BigDecimal.valueOf(allItems.size())))
                .page(page)
                .items(BigDecimal.valueOf(allItems.size()))
                .build(),
                allItems.stream()
                        .skip(page * pageSize)
                        .limit(pageSize)
                        .toList());
    }
}
