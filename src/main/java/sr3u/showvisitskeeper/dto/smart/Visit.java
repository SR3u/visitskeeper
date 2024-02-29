package sr3u.showvisitskeeper.dto.smart;

import lombok.Getter;
import sr3u.showvisitskeeper.dto.smart.annotations.DbList;
import sr3u.showvisitskeeper.dto.smart.annotations.DbObject;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.entities.VisitEntity;

@Getter
public class Visit extends VisitEntity {

    final DbObject<Person> conductor = new DbObject<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getConductorId,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);

    final DbObject<Person> director = new DbObject<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getDirectorId,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);

    final DbList<Person> attendees = new DbList<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getAttendeeIds,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);

    final DbList<Person> artists = new DbList<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getArtistIds,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);

    final DbObject<Venue> venue = new DbObject<>(
            RepositoryHolder.INSTANCE::getVenueRepository,
            this::getVenueId,
            RepositoryHolder.INSTANCE.getMapper()::toVenue);

}
