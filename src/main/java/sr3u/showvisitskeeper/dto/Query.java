package sr3u.showvisitskeeper.dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static sr3u.showvisitskeeper.dto.EntityType.*;

public interface Query {
    String getSearchString();

    Long getSkip();

    Long getLimit();

    static StreamQuery.StreamQueryBuilder streamQuery() {
        return StreamQuery.builder();
    }

    static PagedQuery.PagedQueryBuilder pagedQuery() {
        return PagedQuery.builder();
    }

    default List<String> getEntityOrder() {
        return Stream.of(
                PERSON,
                VENUE,
                COMPOSITION_TYPE,
                COMPOSITION,
                VISIT
        ).map(EntityType::getValue).collect(Collectors.toList());
    }

    ;
}
