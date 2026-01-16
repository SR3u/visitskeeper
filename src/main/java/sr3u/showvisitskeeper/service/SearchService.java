package sr3u.showvisitskeeper.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.PagedQuery;
import sr3u.showvisitskeeper.dto.PagesInfo;
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

@Slf4j
@Service
public class SearchService {
    @Autowired
    List<Searcher> searchers;

    public Result search(Query query) {
        PagesInfo pagesInfo = null;
        if (query instanceof PagedQuery) {
            pagesInfo = pages(query, ((PagedQuery) query).getPageSize());
        }
        return stats(query, searchStream(query)
                .skip(query.getSkip())
                .limit(query.getLimit())
                .collect(Collectors.toList()),
                pagesInfo);
    }

    public Streamex<? extends SearchListEntity> searchStream(Query query) {
        return Streamex.ofCollection(query.getEntityOrder())
                .map(EntityType::fromValue)
                .filter(Objects::nonNull)
                .map(this::findSearcher)
                .filter(Objects::nonNull)
                .flatMap(searcher -> searcher.find(query));
    }

    private Result stats(Query query, List<SearchListEntity> results, PagesInfo pagesInfo) {
        return Result.builder()
                .memes(results)
                .query(query)
                // .stats(stats)
                .pagesInfo(pagesInfo)
                .build();
    }

    public PagesInfo pages(Query query, long pageSize) {
        if (pageSize == 0) {
            pageSize = Searcher.DEFAULT_PAGE_SIZE;
        }
        BigDecimal count = BigDecimal.valueOf(searchStream(query).count());
        long pages = getPagesCount(pageSize, count);
        return PagesInfo.builder().pages(pages).items(count).build();
    }

    public static long getPagesCount(long pageSize, BigDecimal count) {
        return count.divide(BigDecimal.valueOf(pageSize), RoundingMode.CEILING).longValue();
    }

    @Value
    @AllArgsConstructor
    @Builder
    public static class Result {
        Collection<SearchListEntity> memes;
        Query query;
        //SearchStats stats;
        PagesInfo pagesInfo;

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
