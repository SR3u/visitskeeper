package sr3u.showvisitskeeper.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.SearchListEntity;
import sr3u.showvisitskeeper.dto.EntityType;
import sr3u.showvisitskeeper.dto.Query;
import sr3u.streamz.streams.Streamex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    List<Searcher> searchers;

    public Result search(Query query) {
        return stats(query, searchStream(query)
                .skip(query.getSkip())
                .limit(query.getLimit())
                .collect(Collectors.toList()));
    }

    public Streamex<? extends SearchListEntity> searchStream(Query query){
        return Streamex.ofCollection(query.getEntityOrder())
                .map(EntityType::fromValue)
                .filter(Objects::nonNull)
                .map(this::findSearcher)
                .filter(Objects::nonNull)
                .flatMap(searcher -> searcher.find(query));
    }

    private Result stats(Query query, List<SearchListEntity> results) {
        return Result.builder()
                .memes(results)
                .query(query)
                // .stats(stats)
                .build();
    }

    public long pages(Query query, long pageSize) {
        if (pageSize == 0) {
            pageSize = Searcher.DEFAULT_PAGE_SIZE;
        }
        BigDecimal count = BigDecimal.valueOf(searchStream(query).count());
        return count.divide(BigDecimal.valueOf(pageSize), RoundingMode.CEILING).longValue();
    }

    @Value
    @AllArgsConstructor
    @Builder
    public static class Result {
        Collection<SearchListEntity> memes;
        Query query;
        //SearchStats stats;

        public Collection<SearchListEntity> getResults() {
            return getMemes();
        }
    }

    private Searcher findSearcher(EntityType entityType) {
        return searchers.stream()
                .filter(searcher -> searcher.getEntityType() == entityType)
                .findFirst().orElse(null);
    }
}
