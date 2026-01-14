package sr3u.showvisitskeeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.smart.Composition;
import sr3u.showvisitskeeper.dto.smart.Person;
import sr3u.showvisitskeeper.dto.smart.Visit;
import sr3u.showvisitskeeper.dto.smart.annotations.Mapper;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;
import sr3u.showvisitskeeper.exceptions.NotFoundException;
import sr3u.showvisitskeeper.repo.CompositionRepository;
import sr3u.showvisitskeeper.repo.PersonRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompositionService {
    private final CompositionRepository compositionRepository;
    private final VisitService visitService;
    private final Mapper mapper;

    public Composition compositionInfo(UUID id) {
        return RepositoryHolder.INSTANCE.getMapper().toComposition(compositionRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    public List<Composition> find(UUID id, UUID venueId, UUID directorId, UUID conductorId, UUID composerId, UUID visitId, UUID artistId, UUID attendeeId) {
        List<Composition> res = new ArrayList<>();
        if (id != null) {
            res.add(compositionInfo(id));
        }
        if (composerId != null) {
            res.addAll(compositionRepository.findByComposerId(composerId).stream().map(mapper::toComposition).toList());
        }
        List<UUID> compositionIds = visitService.find(visitId, venueId, directorId, conductorId, null, null, artistId, attendeeId).stream().map(VisitEntity::getCompositionId).distinct().toList();
        if (!compositionIds.isEmpty()) {
            res.addAll(compositionRepository.findAllById(compositionIds).stream().map(mapper::toComposition).toList());
        }
        return res.stream().sorted(Comparator.comparing(CompositionEntity::getName)).toList();
    }
}
