package sr3u.showvisitskeeper.service;

import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.dto.EntityType;
import sr3u.showvisitskeeper.dto.Query;
import sr3u.streamz.streams.Streamex;

public interface Searcher {
    long DEFAULT_PAGE_SIZE = 25;

    EntityType getEntityType();

    Streamex<SearchListEntity> find(Query query);
}
