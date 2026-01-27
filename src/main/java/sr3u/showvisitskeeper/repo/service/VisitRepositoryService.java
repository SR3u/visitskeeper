package sr3u.showvisitskeeper.repo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.ProductionEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;
import sr3u.showvisitskeeper.repo.repositories.ProductionRepository;
import sr3u.showvisitskeeper.repo.repositories.VisitRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class VisitRepositoryService implements BaseRepositoryService<VisitEntity> {

    @Autowired
    VisitRepository repository;

    @Autowired
    ProductionRepositoryService productionRepository;


    public List<VisitEntity> findByDateBetween(LocalDate start, LocalDate end) {
        return repository.findByDateBetween(start, end);
    }

    public List<VisitEntity> findByPerceptionHash(String pHash) {
        return repository.findByPerceptionHash(pHash);
    }

    public List<VisitEntity> findByAttendeeIdsIn(Collection<UUID> personIds) {
        return repository.findByAttendeeIdsIn(personIds);
    }

    public List<VisitEntity> findByArtistIdsIn(Collection<UUID> personIds) {
        return repository.findByArtistIdsIn(personIds);
    }

    public List<VisitEntity> findByConductorIdIn(Collection<UUID> personIds) {
        return repository.findByConductorIdIn(personIds);
    }

    public List<VisitEntity> findByDirectorIdIn(Collection<UUID> personIds){
        Set<UUID> productionIds = productionRepository.findByDirectorIdsIn(personIds).stream().map(ProductionEntity::getId).collect(Collectors.toSet());
        return repository.findByProductionIdsIn(productionIds);
    }

    public List<VisitEntity> findByVenueIdIn(Collection<UUID> venueIds){
        return repository.findByVenueIdIn(venueIds);
    }

    public List<VisitEntity> findByCompositionIdsIn(Set<UUID> compositionIds) {
        return repository.findByCompositionIdsIn(compositionIds);
    }

    @Override
    public Optional<VisitEntity> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<VisitEntity> findAllById(Iterable<UUID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public VisitEntity saveAndFlush(VisitEntity entity) {
        return repository.saveAndFlush(entity);
    }

    public List<VisitEntity> findByProductionIdsIn(Collection<UUID> productionIds) {
        return repository.findByProductionIdsIn(productionIds);
    }
}
