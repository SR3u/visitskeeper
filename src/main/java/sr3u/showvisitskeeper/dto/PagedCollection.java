package sr3u.showvisitskeeper.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Getter
@Builder
@RequiredArgsConstructor
@ToString
public class PagedCollection<T> {
  private final PagesInfo pages;
  private final Collection<T> content;
}
