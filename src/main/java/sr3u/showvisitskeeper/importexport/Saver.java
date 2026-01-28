package sr3u.showvisitskeeper.importexport;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sr3u.showvisitskeeper.Tables;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.ProductionEntity;
import sr3u.showvisitskeeper.entities.VenueEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;
import sr3u.showvisitskeeper.repo.repositories.ProductionRepository;
import sr3u.showvisitskeeper.repo.service.CompositionTypeRepositoryService;
import sr3u.showvisitskeeper.repo.service.CompositionRepositoryService;
import sr3u.showvisitskeeper.repo.service.PersonRepositoryService;
import sr3u.showvisitskeeper.repo.service.ProductionRepositoryService;
import sr3u.showvisitskeeper.repo.service.VenueRepositoryService;
import sr3u.showvisitskeeper.repo.service.VisitRepositoryService;
import sr3u.streamz.streams.Streamex;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import java.util.stream.Stream;

@Slf4j
@Service
public class Saver {
    public static final String SEPARATOR = ";";
    @Autowired
    private PersonRepositoryService personRepository;
    @Autowired
    private VenueRepositoryService venueRepository;
    @Autowired
    private CompositionTypeRepositoryService compositionTypeRepository;
    @Autowired
    private CompositionRepositoryService compositionRepository;
    @Autowired
    private ProductionRepositoryService productionRepository;
    @Autowired
    private VisitRepositoryService visitRepository;


    public void save(List<ImportItem> importItems) {
        int deltaSize = importItems.size() / 100;
        for (int i = 0; i < importItems.size(); i++) {
            save(importItems.get(i));
            if (i % deltaSize == 0) {
                log.info("Saved {} of {}", i, importItems.size());
            }
        }
        log.info("Saved {} of {}", importItems.size(), importItems.size());
    }

    @Transactional
    public synchronized void save(ImportItem item) {
        //noinspection unchecked
        Pair<Set<UUID>, Set<UUID>> compositionsAndProductions = Optional.ofNullable(saveCompositions(item)).orElse(Pair.of(Collections.EMPTY_SET, Collections.EMPTY_SET));
        VisitEntity visitEntity = VisitEntity.builder()
                //.id(UUID.randomUUID())
                .venueId(saveVenue(item.getVenue()))
                .productionIds(compositionsAndProductions.getRight())
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
                .notes(item.getAdditionalInfo().orElse(null))
                .artistIds(savePersons(PersonEntity.Type.ACTOR, item, ImportItem::getActors))
                .attendeeIds(join(
                        savePersons(PersonEntity.Type.FAMILY, item, ImportItem::getAttendees),
                        savePersons(PersonEntity.Type.OTHER, item, ImportItem::getAdditionalAttendees))
                )
                .conductorId(savePerson(PersonEntity.Type.CONDUCTOR, item, ImportItem::getConductor))
                .build();

        visitRepository.saveAndFlush(visitEntity);
        //System.out.println(visitEntity);

    }

    private String calculatePHash(VisitEntity visitEntity) {
        List<? extends Serializable> components = Stream.concat(
                Stream.of(visitEntity.getDate()),
                Stream.concat(
                        Optional.ofNullable(visitEntity.getProductionIds()).orElse(Collections.emptySet()).stream(),
                        Stream.of(visitEntity.getVenueId())
                )
        ).toList();
        return trimToSize(Tables.Visit.PHASH_LENGTH, components.stream()
                .map(Optional::ofNullable)
                .map(o -> o.map(Object::toString))
                .map(o -> o.orElse("null"))
                .collect(Collectors.joining(" ")));
    }

    private String trimToSize(int maxLength, String inputString) {
        return inputString.substring(0, Math.min(inputString.length(), maxLength));
    }

    private Pair<Set<UUID>, Set<UUID>> saveCompositions(ImportItem item) {
        Optional<String> showNameO = item.getShowName();
        if (showNameO.isEmpty()) {
            return null;
        }
        List<String> composerShortNames = new ArrayList<>(item.getComposer().map(s -> s.split(SEPARATOR)).stream().flatMap(Arrays::stream).map(String::trim).toList());
        if(composerShortNames.isEmpty()) {
            composerShortNames.add("Неизвестный Композитор");
        }
        List<UUID> composerIds = Streamex.ofCollection(composerShortNames).map(this::saveComposer).stream().toList();
        List<String> showNames = item.getShowName().map(s -> s.split(SEPARATOR)).stream().flatMap(Arrays::stream).toList();
        if (composerIds.size() != showNames.size() && !composerIds.isEmpty() && showNames.size() != 1) {
            throw new IllegalArgumentException("Строка: " + item.getDate().orElse(null) + ": Ошибка: количество произведений и авторов не сходится!");
        }
        Set<UUID> compositionIds = new HashSet<>();
        Set<UUID> productionIds = new HashSet<>();
        UUID directorId = savePerson(PersonEntity.Type.DIRECTOR, item,
                importItem -> Optional.of(Optional.ofNullable(importItem)
                        .flatMap(ImportItem::getDirector)
                        .orElse("Неизвестный Режиссёр")));
        for (int i = 0; i < showNames.size(); i++) {
            String showName = showNames.get(i);

            if (showNames.size() == 1 && !composerIds.isEmpty()) {
                UUID savedCompositionId = saveComposition(item, showName, new HashSet<>(composerIds));
                compositionIds.add(savedCompositionId);
                UUID savedProductionId = saveProduction(savedCompositionId, directorId);
                productionIds.add(savedProductionId);
            } else {
                UUID composerId = null;
                if (!composerIds.isEmpty()) {
                    composerId = composerIds.get(i % composerIds.size());
                }
                UUID savedCompositionId = saveComposition(item, showName, Collections.singleton(composerId));
                UUID savedProductionId = saveProduction(savedCompositionId, directorId);
                productionIds.add(savedProductionId);
                compositionIds.add(savedCompositionId);
            }
        }

        return Pair.of(compositionIds, productionIds);
    }

    private UUID saveProduction(UUID compositionId, UUID directorId) {
        Collection<ProductionEntity> existing = productionRepository.findByCompositionIdAndDirectorIdsIn(compositionId, Collections.singleton(directorId));
        if (existing.isEmpty()) {
            ProductionEntity entity = ProductionEntity.builder()
                    .compositionId(compositionId)
                    .directorIds(Collections.singleton(directorId))
                    .build();
            entity = productionRepository.saveAndFlush(entity);
            existing = new HashSet<>(Collections.singletonList(entity));
        }
        UUID savedProductionId = existing.stream().findFirst().map(ProductionEntity::getId).orElse(null);
        return savedProductionId;
    }

    private UUID saveComposition(ImportItem item, String showName, Set<UUID> composerIds) {
        String name = Optional.ofNullable(showName).map(String::toLowerCase).orElse(null);
        Collection<CompositionEntity> existing = compositionRepository.findByNameAndComposerIdsIn(name, composerIds);
        if (existing.isEmpty()) {
            CompositionEntity compositionEntity = CompositionEntity.builder()
                    //.id(UUID.randomUUID())
                    .createdAt(LocalDateTime.now())
                    .name(name)
                    .fullName(showName)
                    .typeId(saveCompositionType(item))
                    .composerIds(composerIds)
                    .build();
            compositionEntity = compositionRepository.saveAndFlush(compositionEntity);
            existing = new HashSet<>(Collections.singletonList(compositionEntity));
        }
        UUID savedCompositionId = existing.stream().findFirst().map(CompositionEntity::getId).orElse(null);
        return savedCompositionId;
    }

    private UUID saveComposer(String shortName) {
        return savePerson(PersonEntity.Type.COMPOSER, shortName);
    }

    private UUID savePerson(PersonEntity.Type type, final String shortName) {
        return savePerson(type, null, i -> Optional.ofNullable(shortName));
    }

    private UUID saveCompositionType(ImportItem item) {
        Optional<String> type = item.getType();
        if (type.isEmpty()) {
            return null;
        }
        String shortName = type.get().toLowerCase();
        Collection<CompositionTypeEntity> existing = compositionTypeRepository.findByShortName(shortName);
        if (existing.isEmpty()) {
            CompositionTypeEntity typeEntity = CompositionTypeEntity.builder()
                    //.id(UUID.randomUUID())
                    .shortName(shortName)
                    .value(shortName)
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
