package sr3u.showvisitskeeper.service;

import sr3u.showvisitskeeper.dto.Query;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.entities.DbEntity;
import sr3u.showvisitskeeper.util.FieldSearcher;
import sr3u.streamz.streams.Streamex;

import java.util.Collection;
import java.util.Comparator;

public abstract class SearcherImpl<REPO, ENTITY extends DbEntity> implements Searcher {
    @Override
    public Streamex<SearchListEntity> find(Query query) {
        Streamex<ENTITY> stream = Streamex.empty();
        for (FieldSearcher<REPO, ENTITY> searcher : getFieldSearchers()) {
            stream = Streamex.concat(stream, Streamex.ofCollection(searcher.search(getRepository(), query)));
        }
        return stream
                .distinct()
                .sorted(entityComparator())
                .map(this::itemToEntity);
    }

    Comparator<ENTITY> entityComparator() {
        return Comparator.comparing(DbEntity::getDisplayName);
    }

    abstract REPO getRepository();

    abstract Collection<FieldSearcher<REPO, ENTITY>> getFieldSearchers();

    abstract SearchListEntity itemToEntity(ENTITY entity);
}
