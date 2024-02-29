package sr3u.showvisitskeeper.dto.smart;

import lombok.Getter;
import sr3u.showvisitskeeper.dto.smart.annotations.DbObject;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.entities.CompositionEntity;

@Getter
public class Composition extends CompositionEntity {

    final DbObject<Person> composer = new DbObject<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getComposerId,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);
    final DbObject<CompositionType> type = new DbObject<>(
            RepositoryHolder.INSTANCE::getCompositionTypeRepository,
            this::getTypeId,
            RepositoryHolder.INSTANCE.getMapper()::toCompositionType);

}
