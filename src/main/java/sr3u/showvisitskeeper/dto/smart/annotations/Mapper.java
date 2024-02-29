package sr3u.showvisitskeeper.dto.smart.annotations;

import sr3u.showvisitskeeper.dto.smart.CompositionType;
import sr3u.showvisitskeeper.dto.smart.Person;
import sr3u.showvisitskeeper.dto.smart.Venue;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.VenueEntity;

@org.mapstruct.Mapper
public interface Mapper {
    Person toPerson(PersonEntity entity);

    CompositionType toCompositionType(CompositionTypeEntity entity);

    Venue toVenue(VenueEntity o);
}
