package sr3u.showvisitskeeper.util;

import sr3u.showvisitskeeper.dto.Query;
import sr3u.showvisitskeeper.entities.DbEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.repo.PersonRepository;

import java.util.Collection;
import java.util.function.BiFunction;

public interface FieldSearcher<REPO, ENTITY extends DbEntity> extends BiFunction<REPO, Query, Collection<ENTITY>> {
    default Collection<ENTITY> search(REPO repo, Query query) {
        return apply(repo, query);
    }
}
