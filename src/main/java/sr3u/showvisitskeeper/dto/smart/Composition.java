package sr3u.showvisitskeeper.dto.smart;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.dto.smart.annotations.DbList;
import sr3u.showvisitskeeper.dto.smart.annotations.DbObject;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.CompositionEntity.CompositionEntityBuilder;

@Getter
@SuperBuilder
public class Composition extends CompositionEntity {

    private final String _type = "composition";


    private final DbList<Person> composers = new DbList<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getComposerIds,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);
    private final DbObject<CompositionType> type = new DbObject<>(
            RepositoryHolder.INSTANCE::getCompositionTypeRepository,
            this::getTypeId,
            RepositoryHolder.INSTANCE.getMapper()::toCompositionType);

}
