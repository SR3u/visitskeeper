package sr3u.showvisitskeeper.importexport;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor
@ToString
public class ImportItem {

    public static final int FIELDS_COUNT = 13;

    private final Optional<LocalDate> date;
    private final Optional<String> type;
    private final Optional<String> venue;
    private final Optional<String> placeType;
    private final Optional<BigDecimal> ticketPrice;
    private final Set<String> attendees;
    private final Optional<String> showName;
    private final Optional<String> composer;
    private final Optional<String> conductor;
    private final Set<String> actors;
    private final Optional<String> director;
    private final Set<String> additionalAttendees;
    private final Optional<String> additionalInfo;

}
