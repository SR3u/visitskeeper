package sr3u.showvisitskeeper.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@RequiredArgsConstructor
@ToString
public class PagesInfo {
    final long pages;
    final BigDecimal items;
}
