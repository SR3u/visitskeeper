package sr3u.showvisitskeeper.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.Tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = Tables.Persons._TABLE_NAME)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PersonEntity implements DbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = Tables.Persons.ID)
    private UUID id;

    @Column(name = Tables.Persons.SHORT_NAME)
    private String shortName;

    @Column(name = Tables.Persons.TYPE)
    private Type type;

    @Column(name = Tables.Persons.CREATED_AT)
    private LocalDateTime createdAt;

    @Column(name = Tables.Persons.FULL_NAME)
    private String fullName;

    public enum Type {
        FAMILY, COMPOSER, CONDUCTOR, ACTOR, DIRECTOR, OTHER
    }

}
