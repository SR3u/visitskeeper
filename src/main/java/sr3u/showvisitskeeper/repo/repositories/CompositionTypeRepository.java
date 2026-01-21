package sr3u.showvisitskeeper.repo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface CompositionTypeRepository extends JpaRepository<CompositionTypeEntity, UUID> {
    Collection<CompositionTypeEntity> findByValue(String value);

    Collection<CompositionTypeEntity> findByShortName(String shortName);

    Collection<CompositionTypeEntity> findByValueContaining(String searchString);

    Collection<CompositionTypeEntity> findByShortNameContaining(String searchString);

}
