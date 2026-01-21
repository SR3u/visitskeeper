package sr3u.showvisitskeeper.repo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.CompositionEntity;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface CompositionRepository extends JpaRepository<CompositionEntity, UUID> {

    Collection<CompositionEntity> findByNameAndComposerIdsIn(String name, Collection<UUID> composerIds);

    Collection<CompositionEntity> findByName(String name);

    Collection<CompositionEntity> findByNameContainingOrTypeIdIn(String name, Collection<UUID> types);

    Collection<CompositionEntity> findByNameContaining(String name);

    Collection<CompositionEntity> findByComposerIds(UUID personId);

    Collection<CompositionEntity> findByTypeId(UUID typeId);
}
