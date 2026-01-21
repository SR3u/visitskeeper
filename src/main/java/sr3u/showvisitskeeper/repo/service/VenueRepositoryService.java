package sr3u.showvisitskeeper.repo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.VenueEntity;
import sr3u.showvisitskeeper.repo.repositories.VenueRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class VenueRepositoryService implements BaseRepositoryService<VenueEntity> {
    @Autowired
    VenueRepository repository;

    public Collection<VenueEntity> findByShortName(String shortName) {
        return repository.findByShortName(shortName);
    }

    public Collection<VenueEntity> findByShortNameContaining(String searchString) {
        return repository.findByShortNameContaining(searchString);
    }

    @Override
    public Optional<VenueEntity> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<VenueEntity> findAllById(Iterable<UUID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public VenueEntity saveAndFlush(VenueEntity entity) {
        return repository.saveAndFlush(entity);
    }
}
