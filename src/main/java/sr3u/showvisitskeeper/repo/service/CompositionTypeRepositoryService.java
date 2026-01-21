package sr3u.showvisitskeeper.repo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;
import sr3u.showvisitskeeper.repo.repositories.CompositionTypeRepository;
import sr3u.showvisitskeeper.repo.service.CompositionTypeRepositoryService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CompositionTypeRepositoryService implements BaseRepositoryService<CompositionTypeEntity> {

    @Autowired
    CompositionTypeRepository repository;

    public Collection<CompositionTypeEntity> findByValue(String value) {
        return repository.findByValue(value);
    }

    public Collection<CompositionTypeEntity> findByShortName(String shortName) {
        return repository.findByShortName(shortName);
    }

    public Collection<CompositionTypeEntity> findByValueContaining(String searchString) {
        return repository.findByValueContaining(searchString);
    }

    public Collection<CompositionTypeEntity> findByShortNameContaining(String searchString) {
        return repository.findByShortNameContaining(searchString);
    }

    public CompositionTypeEntity saveAndFlush(CompositionTypeEntity typeEntity) {
        return repository.saveAndFlush(typeEntity);
    }

    public Optional<CompositionTypeEntity> findById(UUID compositionType) {
        return repository.findById(compositionType);
    }

    @Override
    public List<CompositionTypeEntity> findAllById(Iterable<UUID> ids) {
        return repository.findAllById(ids);
    }
}
