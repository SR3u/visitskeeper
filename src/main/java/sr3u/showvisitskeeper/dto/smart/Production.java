package sr3u.showvisitskeeper.dto.smart;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.dto.smart.annotations.DbList;
import sr3u.showvisitskeeper.dto.smart.annotations.DbObject;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.entities.CompositionEntity;
import sr3u.showvisitskeeper.entities.DbEntity;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.entities.ProductionEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class Production extends ProductionEntity {
    private final String _type = "production";


    private final DbList<Person> directors = new DbList<>(
            RepositoryHolder.INSTANCE::getPersonRepository,
            this::getDirectorIds,
            RepositoryHolder.INSTANCE.getMapper()::toPerson);
    private final DbObject<Composition> composition = new DbObject<>(
            RepositoryHolder.INSTANCE::getCompositionRepository,
            this::getCompositionId,
            RepositoryHolder.INSTANCE.getMapper()::toComposition);

    public String getDisplayName() {
        Optional<CompositionEntity> composition = Optional.ofNullable(this.composition.get());
        String compositionName = composition.map(DbEntity::getDisplayName).orElse("???");
        List<Person> directors = Optional.of(this.directors.get()).orElseGet(Collections::emptyList);
        String directorsNames = directors.stream().map(DbEntity::getDisplayName).collect(Collectors.joining(","));
        return compositionName + " (" + directorsNames + ")";
//        return getCompositionId()+" ("+ getDirectorIds().stream().map(String::valueOf).collect(Collectors.joining(","))+")";
    }
}
