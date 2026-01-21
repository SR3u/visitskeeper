package sr3u.showvisitskeeper.repo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.VenueEntity;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, UUID> {
    Collection<VenueEntity> findByShortName(String shortName);

    Collection<VenueEntity> findByShortNameContaining(String searchString);
}
