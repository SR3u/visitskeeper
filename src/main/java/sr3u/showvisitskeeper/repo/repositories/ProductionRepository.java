package sr3u.showvisitskeeper.repo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sr3u.showvisitskeeper.entities.ProductionEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ProductionRepository extends JpaRepository<ProductionEntity, UUID> {
    Collection<ProductionEntity> findByCompositionIdAndDirectorIdsIn(UUID compositionId, Collection<UUID> directorIds);

    Collection<ProductionEntity> findByDirectorIdsIn(Collection<UUID> personIds);

    List<ProductionEntity> findByCompositionIdIn(Collection<UUID> compositionIds);
}
