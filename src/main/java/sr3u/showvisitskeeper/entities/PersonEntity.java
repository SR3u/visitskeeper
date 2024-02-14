package sr3u.showvisitskeeper.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sr3u.showvisitskeeper.Tables;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = Tables.Persons._TABLE_NAME)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = Tables.Persons.ID)
    private UUID id;

    @Column(name = Tables.Persons.SHORT_NAME)
    private String shortName;

    @Column(name = Tables.Persons.TYPE)
    private Type type;

    @Column(name = Tables.Persons.CREATED_AT)
    private LocalDateTime createdAt;

    public enum Type{
        FAMILY, COMPOSER, CONDUCTOR, ACTOR, DIRECTOR, OTHER
    }
}
