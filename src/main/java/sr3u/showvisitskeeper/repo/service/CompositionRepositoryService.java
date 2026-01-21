package sr3u.showvisitskeeper.repo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.repo.repositories.CompositionRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CompositionRepositoryService implements BaseRepositoryService<CompositionEntity> {

    @Autowired
    CompositionRepository repository;

    public Collection<CompositionEntity> findByNameAndComposerIdsIn(String name, Collection<UUID> composerId) {
        return repository.findByNameAndComposerIdsIn(name, composerId);
    }

    public Collection<CompositionEntity> findByName(String name) {
        return repository.findByName(name);
    }

    public Collection<CompositionEntity> findByNameContainingOrTypeIdIn(String name, Collection<UUID> types) {
        return repository.findByNameContainingOrTypeIdIn(name, types);
    }

    public Collection<CompositionEntity> findByNameContaining(String name) {
        return repository.findByNameContaining(name);
    }

    public Collection<CompositionEntity> findByComposerIds(UUID personId) {
        return repository.findByComposerIds(personId);
    }

    public Collection<CompositionEntity> findByTypeId(UUID typeId) {
        return repository.findByTypeId(typeId);
    }


    public CompositionEntity saveAndFlush(CompositionEntity compositionEntity) {
        return repository.saveAndFlush(compositionEntity);
    }

    public Optional<CompositionEntity> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<CompositionEntity> findAllById(Iterable<UUID> ids) {
        return repository.findAllById(ids);
    }

    public List<CompositionEntity> findAllById(Collection<UUID> compositionIds) {
        return repository.findAllById(compositionIds);
    }
}
