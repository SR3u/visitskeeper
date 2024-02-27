package sr3u.showvisitskeeper.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CompositionRepository extends JpaRepository<CompositionEntity, UUID> {
    Collection<CompositionEntity> findByNameAndComposerId(String Name, UUID composerId);

    Collection<CompositionEntity> findByName(String name);

    Collection<CompositionEntity> findByNameContainingOrTypeIn(String name, Collection<UUID> types);

    Collection<CompositionEntity> findByNameContaining(String name);

    Collection<CompositionEntity> findByComposerId(UUID personId);
}
