package sr3u.showvisitskeeper.dto.smart;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.entities.PersonEntity;

@SuperBuilder
@Getter
public class Person extends PersonEntity {
    private final String _type = "person";
}
