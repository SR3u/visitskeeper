package sr3u.showvisitskeeper.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.VenueEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<VisitEntity, UUID> {
    Collection<VisitEntity> findByDateBetween(LocalDate start, LocalDate end);

    Collection<VisitEntity> findByPerceptionHash(String pHash);

    Collection<VisitEntity> findByAttendeeIdsIn(Collection<UUID> personIds);

    Collection<VisitEntity> findByArtistIdsIn(Collection<UUID> personIds);

    Collection<VisitEntity> findByConductorIdIn(Collection<UUID> personIds);

    Collection<VisitEntity> findByDirectorIdIn(Collection<UUID> personIds);

    Collection<VisitEntity> findByVenueIdIn(Collection<UUID> venueIds);

    Collection<VisitEntity> findByCompositionIdIn(List<UUID> compositionIds);
}
