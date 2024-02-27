package sr3u.showvisitskeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class StreamQuery implements Query {
    String searchString;

    @Builder.Default
    Long skip = 0L;
    @Builder.Default
    Long limit = 100L;
}