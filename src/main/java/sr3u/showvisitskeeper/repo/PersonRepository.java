package sr3u.showvisitskeeper.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.PersonEntity;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, UUID> {
    Collection<PersonEntity> findByShortName(String shortName);

    Collection<PersonEntity> findByShortNameContaining(String shortName);

}
