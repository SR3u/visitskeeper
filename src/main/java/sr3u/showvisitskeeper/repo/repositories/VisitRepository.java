package sr3u.showvisitskeeper.repo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.VisitEntity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<VisitEntity, UUID> {
    List<VisitEntity> findByDateBetween(LocalDate start, LocalDate end);

    List<VisitEntity> findByPerceptionHash(String pHash);

    List<VisitEntity> findByAttendeeIdsIn(Collection<UUID> personIds);

    List<VisitEntity> findByArtistIdsIn(Collection<UUID> personIds);

    List<VisitEntity> findByConductorIdIn(Collection<UUID> personIds);

    List<VisitEntity> findByVenueIdIn(Collection<UUID> venueIds);

    List<VisitEntity> findByProductionIdsIn(Collection<UUID> productionIds);
}
