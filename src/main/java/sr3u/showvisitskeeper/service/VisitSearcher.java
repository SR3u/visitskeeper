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
import sr3u.showvisitskeeper.repo.CompositionRepository;
import sr3u.showvisitskeeper.repo.PersonRepository;
import sr3u.showvisitskeeper.repo.VenueRepository;
import sr3u.showvisitskeeper.repo.VisitRepository;
import sr3u.streamz.streams.Streamex;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class VisitSearcher implements Searcher {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    CompositionRepository compositionRepository;
    @Autowired
    VisitRepository visitRepository;
    @Autowired
    VenueRepository venueRepository;

    public static final java.text.SimpleDateFormat DATE_FMT = new java.text.SimpleDateFormat("yyyy-MM-dd");

    @Override
    public EntityType getEntityType() {
        return EntityType.VISIT;
    }

    @Override
    public Streamex<SearchListEntity> find(Query query) {
        List<UUID> personIds = Streamex.ofCollection(personRepository.findByShortNameContaining(query.getSearchString()))
                .map(PersonEntity::getId).stream().toList();
        List<UUID> compositionIds = Streamex.ofCollection(compositionRepository.findByNameContaining(query.getSearchString()))
                .map(CompositionEntity::getId).stream().toList();
        return joinStreams(
                visitRepository.findByCompositionIdIn(compositionIds),
                visitRepository.findByConductorIdIn(personIds),
                visitRepository.findByDirectorIdIn(personIds),
                visitRepository.findByArtistIdsIn(personIds),
                visitRepository.findByAttendeeIdsIn(personIds))
                .distinct()
                .sorted(Comparator.comparing(VisitEntity::getDate))
                .map(this::toEntity);
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
                        .map(DATE_FMT::format).orElse(""))
                .description(description(v))
                .url("/html/visit?id=" + v.getId())
                .build();
    }

    private String description(VisitEntity v) {
        String description = "";
        description += compositionRepository.findById(v.getCompositionId()).map(CompositionEntity::getName).map(this::ws).orElse("");
        description += venueRepository.findById(v.getVenueId()).map(VenueEntity::getDisplayName).map(this::ws).orElse("");
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
