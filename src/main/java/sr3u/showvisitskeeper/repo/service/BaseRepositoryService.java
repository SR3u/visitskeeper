package sr3u.showvisitskeeper.repo.service;

import sr3u.showvisitskeeper.entities.CompositionEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseRepositoryService<T> {
    Optional<T> findById(UUID id);

    List<T> findAllById(Iterable<UUID> ids);

    T saveAndFlush(T entity);
}
