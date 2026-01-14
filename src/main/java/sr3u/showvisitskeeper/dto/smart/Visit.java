package sr3u.showvisitskeeper.dto.smart;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.dto.smart.annotations.DbList;
import sr3u.showvisitskeeper.dto.smart.annotations.DbObject;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.entities.VisitEntity;

@Getter
@SuperBuilder
public class Visit extends VisitEntity {
    private final String _type = "visit";


    private final DbObject<Person> conductor = new DbObject<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getConductorId,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);


    private final DbObject<Person> director = new DbObject<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getDirectorId,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);


    private final DbList<Person> attendees = new DbList<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getAttendeeIds,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);


    private final DbList<Person> artists = new DbList<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getArtistIds,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);


    private final DbObject<Venue> venue = new DbObject<>(
            RepositoryHolder.INSTANCE::getVenueRepository,
            this::getVenueId,
            RepositoryHolder.INSTANCE.getMapper()::toVenue);


    private final DbObject<Composition> composition = new DbObject<>(
            RepositoryHolder.INSTANCE::getCompositionRepository,
            this::getCompositionId,
            RepositoryHolder.INSTANCE.getMapper()::toComposition);

}
