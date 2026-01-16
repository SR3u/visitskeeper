package sr3u.showvisitskeeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.PagedCollection;
import sr3u.showvisitskeeper.dto.PagesInfo;
import sr3u.showvisitskeeper.dto.smart.Composition;
import sr3u.showvisitskeeper.dto.smart.annotations.Mapper;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;
import sr3u.showvisitskeeper.exceptions.NotFoundException;
import sr3u.showvisitskeeper.repo.CompositionRepository;

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
    private final CompositionRepository compositionRepository;
    private final VisitService visitService;
    private final Mapper mapper;

    public Composition compositionInfo(UUID id) {
        return RepositoryHolder.INSTANCE.getMapper().toComposition(compositionRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    public PagedCollection<Composition> find(UUID id, UUID venueId, UUID directorId, UUID conductorId, UUID composerId, UUID visitId, UUID artistId, UUID attendeeId, long pageSize, long page) {
        List<Composition> res = new ArrayList<>();
        if (id != null) {
            res.add(compositionInfo(id));
        }
        if (composerId != null) {
            res.addAll(compositionRepository.findByComposerId(composerId).stream().map(mapper::toComposition).toList());
        }
        List<UUID> compositionIds = visitService.find(visitId, venueId, directorId, conductorId, null, null, artistId, attendeeId, -1, -1)
                .getContent().stream()
                .map(VisitEntity::getCompositionId).distinct().toList();
        if (!compositionIds.isEmpty()) {
            res.addAll(compositionRepository.findAllById(compositionIds).stream().map(mapper::toComposition).toList());
        }
        return paging(res.stream().sorted(Comparator.comparing(CompositionEntity::getName)).toList(), pageSize, page);
    }

    public static <T> PagedCollection<T> paging(Collection<T> allItems, long pageSize, long page) {
        if(page < 0 && pageSize <=0) {
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
