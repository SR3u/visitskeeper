package sr3u.showvisitskeeper.dto;


import lombok.Builder;
import lombok.Value;
import sr3u.showvisitskeeper.service.Searcher;

@Value
@Builder
public class PagedQuery implements Query {
    String searchString;

    long page;
    Long pageSize;

    public PagedQuery(String searchString, Long page, Long pageSize) {
        this.searchString = searchString;
        if (page == null) {
            page = 0L;
        }
        this.page = page;
        if (pageSize == null) {
            pageSize = Searcher.DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }

    @Override
    public Long getSkip() {
        return page * pageSize;
    }

    @Override
    public Long getLimit() {
        return pageSize;
    }
}
