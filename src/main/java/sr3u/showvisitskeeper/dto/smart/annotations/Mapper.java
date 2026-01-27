package sr3u.showvisitskeeper.dto.smart.annotations;

import sr3u.showvisitskeeper.dto.smart.Composition;
import sr3u.showvisitskeeper.dto.smart.CompositionType;
import sr3u.showvisitskeeper.dto.smart.Person;
import sr3u.showvisitskeeper.dto.smart.Production;
import sr3u.showvisitskeeper.dto.smart.Venue;
import sr3u.showvisitskeeper.dto.smart.Visit;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.ProductionEntity;
import sr3u.showvisitskeeper.entities.VenueEntity;
import sr3u.showvisitskeeper.entities.VisitEntity;

@org.mapstruct.Mapper
public interface Mapper {
    Person toPerson(PersonEntity entity);

    CompositionType toCompositionType(CompositionTypeEntity entity);

    Venue toVenue(VenueEntity o);

    Composition toComposition(CompositionEntity compositionEntity);

    Visit toVisit(VisitEntity visitEntity);

    Production toProduction(ProductionEntity productionEntity);

}
