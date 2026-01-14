package sr3u.showvisitskeeper.importexport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.VenueEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;
import sr3u.showvisitskeeper.repo.CompositionRepository;
import sr3u.showvisitskeeper.repo.CompositionTypeRepository;
import sr3u.showvisitskeeper.repo.PersonRepository;
import sr3u.showvisitskeeper.repo.VenueRepository;
import sr3u.showvisitskeeper.repo.VisitRepository;
import sr3u.streamz.streams.Streamex;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class Saver {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private CompositionTypeRepository compositionTypeRepository;
    @Autowired
    private CompositionRepository compositionRepository;
    @Autowired
    private VisitRepository visitRepository;


    public void save(Collection<ImportItem> importItems) {
        Streamex.ofCollection(importItems).forEach(this::save);
    }

    @Transactional
    private synchronized void save(ImportItem item) {
        VisitEntity visitEntity = VisitEntity.builder()
                //.id(UUID.randomUUID())
                .venueId(saveVenue(item.getVenue()))
                .compositionId(saveComposition(item))
                .date(item.getDate().orElse(null))
                .build();
        visitEntity = visitEntity.toBuilder()
                .perceptionHash(calculatePHash(visitEntity))
                .build();
        Collection<VisitEntity> byPHash = visitRepository.findByPerceptionHash(visitEntity.getPerceptionHash());
        if (!byPHash.isEmpty()) {
            visitEntity = byPHash.stream().findFirst().orElseThrow();
        }
        visitEntity = visitEntity.toBuilder()
                .ticketPrice(item.getTicketPrice().orElse(null))
                .details(item.getAdditionalInfo().orElse(null))
                .artistIds(savePersons(PersonEntity.Type.ACTOR, item, ImportItem::getActors))
                .attendeeIds(join(
                        savePersons(PersonEntity.Type.FAMILY, item, ImportItem::getAttendees),
                        savePersons(PersonEntity.Type.OTHER, item, ImportItem::getAdditionalAttendees))
                )
                .conductorId(savePerson(PersonEntity.Type.CONDUCTOR, item, ImportItem::getConductor))
                .directorId(savePerson(PersonEntity.Type.DIRECTOR, item, ImportItem::getDirector))
                .build();

        visitRepository.saveAndFlush(visitEntity);
        //System.out.println(visitEntity);

    }

    private String calculatePHash(VisitEntity visitEntity) {
        List<? extends Serializable> components = Arrays.asList(
                visitEntity.getDate(),
                visitEntity.getCompositionId(),
                visitEntity.getVenueId());
        return components.stream()
                .map(Optional::ofNullable)
                .map(o -> o.map(Object::toString))
                .map(o -> o.orElse("null"))
                .collect(Collectors.joining(" "));
    }

    private UUID saveComposition(ImportItem item) {
        Optional<String> showNameO = item.getShowName();
        if (showNameO.isEmpty()) {
            return null;
        }
        UUID composerId = savePerson(PersonEntity.Type.COMPOSER, item, ImportItem::getComposer);
        String showName = showNameO.get().toLowerCase();
        Collection<CompositionEntity> existing = compositionRepository.findByNameAndComposerId(showName, composerId);
        if (existing.isEmpty()) {
            CompositionEntity compositionEntity = CompositionEntity.builder()
                    //.id(UUID.randomUUID())
                    .createdAt(LocalDateTime.now())
                    .name(showName)
                    .typeId(saveCompositionType(item))
                    .composerId(composerId)
                    .build();
            compositionEntity = compositionRepository.saveAndFlush(compositionEntity);
            existing = new HashSet<>(Collections.singletonList(compositionEntity));
        }
        return existing.stream().findFirst().map(CompositionEntity::getId).orElse(null);
    }

    private UUID saveCompositionType(ImportItem item) {
        Optional<String> type = item.getType();
        if (type.isEmpty()) {
            return null;
        }
        String value = type.get().toLowerCase();
        Collection<CompositionTypeEntity> existing = compositionTypeRepository.findByValue(value);
        if (existing.isEmpty()) {
            CompositionTypeEntity typeEntity = CompositionTypeEntity.builder()
                    //.id(UUID.randomUUID())
                    .value(value)
                    .createdAt(LocalDateTime.now())
                    .build();
            typeEntity = compositionTypeRepository.saveAndFlush(typeEntity);
            existing = new HashSet<>(Collections.singletonList(typeEntity));
        }
        return existing.stream().findFirst().map(CompositionTypeEntity::getId).orElse(null);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private UUID saveVenue(Optional<String> shortName) {
        if (shortName.isEmpty()) {
            return null;
        }
        String shortName1 = shortName.get().toLowerCase();
        Collection<VenueEntity> venues = venueRepository.findByShortName(shortName1);
        if (venues.isEmpty()) {
            VenueEntity venue = VenueEntity.builder()
                    //.id(UUID.randomUUID())
                    .shortName(shortName1)
                    .createdAt(LocalDateTime.now())
                    .build();
            venue = venueRepository.saveAndFlush(venue);
            venues = new HashSet<>(List.of(venue));
        }
        return venues.stream().findFirst().map(VenueEntity::getId).orElseThrow();
    }

    private UUID savePerson(PersonEntity.Type type, ImportItem item, Function<ImportItem, Optional<String>> shortNamesExtractor) {
        String shortName = shortNamesExtractor.apply(item).map(String::toLowerCase).orElse(null);
        if (shortName == null) {
            return null;
        }
        Collection<PersonEntity> found = personRepository.findByShortName(shortName);
        if (found.isEmpty()) {
            PersonEntity person = PersonEntity.builder()
                    //.id(UUID.randomUUID())
                    .type(type)
                    .shortName(shortName)
                    .createdAt(LocalDateTime.now())
                    .build();
            person = personRepository.saveAndFlush(person);
            found = new HashSet<>(Collections.singletonList(person));
        }
        return found.stream().findFirst().map(PersonEntity::getId).orElse(null);
    }

    private Set<UUID> join(Set<UUID> a, Set<UUID> b) {
        Set<UUID> result = new HashSet<>();
        result.addAll(a);
        result.addAll(b);
        return result;
    }

    private Set<UUID> savePersons(PersonEntity.Type type, ImportItem item, Function<ImportItem, Collection<String>> shortNamesExtractor) {
        return shortNamesExtractor.apply(item).stream()
                .map(String::toLowerCase)
                .flatMap(shortName -> {
                    Collection<PersonEntity> found = personRepository.findByShortName(shortName);
                    if (found.isEmpty()) {
                        PersonEntity person = PersonEntity.builder()
                                //.id(UUID.randomUUID())
                                .type(type)
                                .shortName(shortName)
                                .createdAt(LocalDateTime.now())
                                .build();
                        person = personRepository.saveAndFlush(person);
                        found = new HashSet<>(Collections.singletonList(person));
                    }
                    return found.stream();
                })
                .map(PersonEntity::getId)
                .collect(Collectors.toSet());
    }
}
