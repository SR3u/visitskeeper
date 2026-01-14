package sr3u.showvisitskeeper.dto.smart;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.entities.CompositionTypeEntity;


@SuperBuilder
@Getter
public class CompositionType extends CompositionTypeEntity {
    private final String _type = "composition_type";
}
