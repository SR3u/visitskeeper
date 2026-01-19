package sr3u.showvisitskeeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.PagedCollection;
import sr3u.showvisitskeeper.dto.smart.Visit;
import sr3u.showvisitskeeper.dto.smart.annotations.Mapper;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;
import sr3u.showvisitskeeper.exceptions.NotFoundException;
import sr3u.showvisitskeeper.repo.CompositionRepository;
import sr3u.showvisitskeeper.repo.VisitRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static sr3u.showvisitskeeper.service.CompositionService.paging;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final CompositionRepository compositionRepository;
    private final Mapper mapper;

    public Visit visitInfo(UUID id) {
        return RepositoryHolder.INSTANCE.getMapper().toVisit(visitRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    public PagedCollection<Visit> find(UUID id, UUID venueId, UUID directorId, UUID conductorId, UUID composerId, UUID compositionId, UUID artistId, UUID attendeeId, UUID compositionTypeId, long pageSize, long page) {
        List<Visit> res = new ArrayList<>();
        if (id != null) {
            res.add(visitInfo(id));
        }
        if (venueId != null) {
            res.addAll(visitRepository.findByVenueIdIn(List.of(venueId)).stream().map(mapper::toVisit).toList());
        }
        if (conductorId != null) {
            res.addAll(visitRepository.findByConductorIdIn(List.of(conductorId)).stream().map(mapper::toVisit).toList());
        }
        if (directorId != null) {
            res.addAll(visitRepository.findByDirectorIdIn(List.of(directorId)).stream().map(mapper::toVisit).toList());
        }
        if (artistId != null) {
            res.addAll(visitRepository.findByArtistIdsIn(List.of(artistId)).stream().map(mapper::toVisit).toList());
        }
        if (attendeeId != null) {
            res.addAll(visitRepository.findByAttendeeIdsIn(List.of(attendeeId)).stream().map(mapper::toVisit).toList());
        }
        List<UUID> compositionIds = new ArrayList<>();
        if (compositionId != null) {
            compositionIds.add(compositionId);
        }
        if (composerId != null) {
            compositionIds.addAll(compositionRepository.findByComposerId(composerId).stream().map(CompositionEntity::getId).toList());
        }
        if (compositionTypeId != null) {
            compositionIds.addAll(compositionRepository.findByTypeId(compositionTypeId).stream().map(CompositionEntity::getId).toList());
        }
        if (!compositionIds.isEmpty()) {
            res.addAll(visitRepository.findByCompositionIdIn(compositionIds).stream().map(mapper::toVisit).toList());
        }
        return paging(res.stream().sorted(Comparator.comparing(VisitEntity::getDate)).toList(), pageSize, page);
    }
}
