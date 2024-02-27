package sr3u.showvisitskeeper.dto;

import lombok.Getter;

import java.util.Arrays;

public enum EntityType {
    COMPOSITION_TYPE,
    COMPOSITION,
    PERSON,
    VENUE,
    VISIT;

    public String getValue() {
        return this.name();
    }

    public static EntityType fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> value.equalsIgnoreCase(v.getValue()))
                .findFirst().orElse(null);
    }
}
