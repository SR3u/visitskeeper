package sr3u.showvisitskeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.dto.EntityType;
import sr3u.showvisitskeeper.dto.Query;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.VenueEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;
import sr3u.showvisitskeeper.repo.repositories.PersonRepository;
import sr3u.showvisitskeeper.repo.repositories.VenueRepository;
import sr3u.showvisitskeeper.repo.repositories.VisitRepository;
import sr3u.showvisitskeeper.repo.service.CompositionRepositoryService;
import sr3u.showvisitskeeper.repo.service.PersonRepositoryService;
import sr3u.showvisitskeeper.repo.service.VenueRepositoryService;
import sr3u.showvisitskeeper.repo.service.VisitRepositoryService;
import sr3u.streamz.streams.Streamex;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class VisitSearcher implements Searcher {
    @Autowired
    PersonRepositoryService personRepository;
    @Autowired
    CompositionRepositoryService compositionRepository;
    @Autowired
    VisitRepositoryService visitRepository;
    @Autowired
    VenueRepositoryService venueRepository;

    public static final java.text.SimpleDateFormat DATE_FMT = new java.text.SimpleDateFormat("yyyy-MM-dd");

    @Override
    public EntityType getEntityType() {
        return EntityType.VISIT;
    }

    @Override
    public Streamex<SearchListEntity> find(Query query) {
        List<UUID> personIds = Streamex.ofCollection(personRepository.findByShortNameContaining(query.getSearchString()))
                .map(PersonEntity::getId).stream().collect(Collectors.toList());
        List<UUID> compositionIds = Streamex.ofCollection(compositionRepository.findByNameContaining(query.getSearchString()))
                .map(CompositionEntity::getId).stream().collect(Collectors.toList());
        return joinStreams(
                getByCompositionIdIn(compositionIds),
                getByConductorIdIn(personIds),
                getByDirectorIdIn(personIds),
                getByArtistIdsIn(personIds),
                getByAttendeeIdsIn(personIds))
                .distinct()
                .sorted(Comparator.comparing(VisitEntity::getDate))
                .map(this::toEntity);
    }

    private List<VisitEntity> getByAttendeeIdsIn(List<UUID> personIds) {
        return visitRepository.findByAttendeeIdsIn(personIds);
//        return visitRepository.findAll().stream()
//                .filter(v -> v.getAttendeeIds() != null)
//                .filter(v -> personIds.stream()
//                        .anyMatch(pid -> v.getAttendeeIds().contains(pid)))
//                .collect(Collectors.toList());
    }

    private List<VisitEntity> getByArtistIdsIn(List<UUID> personIds) {
        return visitRepository.findByArtistIdsIn(personIds);
//        return visitRepository.findAll().stream()
//                .filter(v -> v.getArtistIds() != null)
//                .filter(v -> personIds.stream()
//                        .anyMatch(pid -> v.getArtistIds().contains(pid)))
//                .collect(Collectors.toList());
    }

    private List<VisitEntity> getByDirectorIdIn(List<UUID> personIds) {
        return visitRepository.findByDirectorIdIn(personIds);
//        return visitRepository.findAll().stream()
//                .filter(v -> v.getDirectorId() != null)
//                .filter(v -> personIds.stream()
//                        .anyMatch(pid -> v.getDirectorId().equals(pid)))
//                .collect(Collectors.toList());
    }

    private List<VisitEntity> getByConductorIdIn(List<UUID> personIds) {
        return visitRepository.findByConductorIdIn(personIds);
//        return visitRepository.findAll().stream()
//                .filter(v -> v.getConductorId() != null)
//                .filter(v -> personIds.stream()
//                        .anyMatch(pid -> v.getConductorId().equals(pid)))
//                .collect(Collectors.toList());
    }

    private List<VisitEntity> getByCompositionIdIn(List<UUID> compositionIds) {
       return visitRepository.findByCompositionIdsIn(new HashSet<>(compositionIds));
//        return visitRepository.findAll().stream()
//                .filter(v -> v.getCompositionId() != null)
//                .filter(v -> compositionIds.stream()
//                        .anyMatch(pid -> v.getCompositionId().equals(pid)))
//                .collect(Collectors.toList());
    }

    @SafeVarargs
    private Streamex<VisitEntity> joinStreams(Collection<VisitEntity>... visits) {
        return Streamex.ofStream(Stream.of(visits)
                .flatMap(Collection::stream));
    }

    private SearchListEntity toEntity(VisitEntity v) {
        return SearchListEntity.builder()
                .id(v.getId())
                .type(getEntityType().getValue())
                .fullName(Optional.ofNullable(v.getDate()).map(this::convertToDateViaInstant)
                        .map(DATE_FMT::format).orElse("unknown"))
                .description(description(v))
                .url("/html/visit?id=" + v.getId())
                .build();
    }

    private String description(VisitEntity v) {
        String description = "";
        if (v.getCompositionIds() != null) {
            description += Optional.of(compositionRepository.findAllById(v.getCompositionIds()).stream()
                            .map(CompositionEntity::getName)
                            .collect(Collectors.joining("; ")))
                    .map(this::ws).orElse("");
        }
        if (v.getVenueId() != null) {
            description += venueRepository.findById(v.getVenueId()).map(VenueEntity::getDisplayName).map(this::ws).orElse("");
        }
        return description;
    }

    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private String ws(String s) {
        return " " + s;
    }
}
