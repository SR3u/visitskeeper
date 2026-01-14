package sr3u.showvisitskeeper.dto.smart;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.entities.VenueEntity;

@SuperBuilder
@Getter
public class Venue extends VenueEntity {
    private final String _type = "venue";
}
