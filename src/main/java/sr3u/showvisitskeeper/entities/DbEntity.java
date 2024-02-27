package sr3u.showvisitskeeper.entities;

import java.util.Optional;

public interface DbEntity {
    String getShortName();

    default String getDisplayName() {
        return Optional.ofNullable(getShortName()).map(String::toUpperCase).orElse("НЕИЗВЕСТНО");
    }
}
