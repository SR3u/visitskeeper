package sr3u.showvisitskeeper.repo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sr3u.showvisitskeeper.entities.ImportEntity;

import java.util.Optional;
import java.util.UUID;

public interface ImportRepository extends JpaRepository<ImportEntity, UUID> {
    Optional<ImportEntity> findByFile(String file);

    void deleteByFile(String file);
}
