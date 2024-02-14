package sr3u.showvisitskeeper.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.VenueEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<VisitEntity, UUID> {
    Collection<VisitEntity> findByDateBetween(LocalDate start, LocalDate end);

    Collection<VisitEntity> findByPerceptionHash(String pHash);

}
