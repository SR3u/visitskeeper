package sr3u.showvisitskeeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.PagedCollection;
import sr3u.showvisitskeeper.dto.PagesInfo;
import sr3u.showvisitskeeper.dto.smart.Composition;
import sr3u.showvisitskeeper.dto.smart.annotations.Mapper;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.ProductionEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;
import sr3u.showvisitskeeper.exceptions.NotFoundException;
import sr3u.showvisitskeeper.repo.service.CompositionRepositoryService;
import sr3u.showvisitskeeper.repo.service.ProductionRepositoryService;
import sr3u.streamz.optionals.Optionalex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static sr3u.showvisitskeeper.service.SearchService.getPagesCount;

@Service
@RequiredArgsConstructor
public class CompositionService {
    private final CompositionRepositoryService compositionRepository;
    private final ProductionRepositoryService productionRepositoryService;
    private final VisitService visitService;
    private final Mapper mapper;

    public Composition compositionInfo(UUID id) {
        return RepositoryHolder.INSTANCE.getMapper().toComposition(compositionRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    public PagedCollection<Composition> find(UUID id, UUID venueId, UUID directorId, UUID conductorId, UUID composerId,
                                             UUID visitId, UUID artistId, UUID attendeeId, UUID compositionTypeId,
                                             UUID productionId,
                                             long pageSize, long page) {
        List<Composition> res = new ArrayList<>();
        if (id != null) {
            res.add(compositionInfo(id));
        }
        if (composerId != null) {
            res.addAll(compositionRepository.findByComposerIds(composerId).stream().map(mapper::toComposition).toList());
        }
        if (compositionTypeId != null) {
            res.addAll(compositionRepository.findByTypeId(compositionTypeId).stream().map(mapper::toComposition).toList());
        }
        List<UUID> compositionIds = new ArrayList<>();
        if (productionId != null) {
            productionRepositoryService.findById(productionId)
                    .map(ProductionEntity::getCompositionId)
                    .ifPresent(compositionIds::add);
        }
        List<UUID> productionIds = visitService.find(visitId, venueId, directorId, conductorId,
                        null, null, artistId, attendeeId, null,
                        null,
                        -1, -1)
                .getContent().stream()
                .map(VisitEntity::getProductionIds)
                .flatMap(Collection::stream)
                .distinct()
                .toList();
        compositionIds.addAll(productionRepositoryService.findAllById(productionIds).stream()
                .map(ProductionEntity::getCompositionId)
                .distinct()
                .toList());
        if (!compositionIds.isEmpty()) {
            res.addAll(compositionRepository.findAllById(compositionIds).stream().map(mapper::toComposition).toList());
        }
        return paging(res.stream().sorted(Comparator.comparing(CompositionEntity::getName)).toList(), pageSize, page);
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
