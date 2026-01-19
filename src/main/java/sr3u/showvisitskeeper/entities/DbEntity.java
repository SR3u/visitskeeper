package sr3u.showvisitskeeper.entities;

import java.util.Optional;

public interface DbEntity {
    String getShortName();

    default String getFullName() {
        return null;
    }

    default String getDisplayName() {
        return Optional.ofNullable(getFullName()).orElseGet(()->Optional.ofNullable(getShortName()).map(String::toUpperCase).orElse("НЕИЗВЕСТНО"));
    }
}
