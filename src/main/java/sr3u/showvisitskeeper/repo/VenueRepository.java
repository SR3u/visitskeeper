package sr3u.showvisitskeeper.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.VenueEntity;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, UUID> {
    Collection<VenueEntity> findByShortName(String shortName);

}
