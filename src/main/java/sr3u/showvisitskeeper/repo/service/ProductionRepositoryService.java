package sr3u.showvisitskeeper.repo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.ProductionEntity;
import sr3u.showvisitskeeper.repo.repositories.ProductionRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductionRepositoryService implements BaseRepositoryService<ProductionEntity> {

    @Autowired
    ProductionRepository repository;

    public Collection<ProductionEntity> findByCompositionIdAndDirectorIdsIn(UUID compositionId, Collection<UUID> directorIds) {
        return repository.findByCompositionIdAndDirectorIdsIn(compositionId, directorIds);
    }

    public Collection<ProductionEntity> findByDirectorIdsIn(Collection<UUID> directorIds) {
        return repository.findByDirectorIdsIn(directorIds);
    }

    @Override
    public Optional<ProductionEntity> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<ProductionEntity> findAllById(Iterable<UUID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public ProductionEntity saveAndFlush(ProductionEntity entity) {
        return repository.saveAndFlush(entity);
    }

    public List<ProductionEntity> findByCompositionIdIn(Collection<UUID> compositionIds) {
        return repository.findByCompositionIdIn(compositionIds);
    }
}
