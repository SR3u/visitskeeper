package sr3u.showvisitskeeper.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.Tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = Tables.Persons.ID)
    private UUID id;

    @Column(name = Tables.Persons.SHORT_NAME)
    private String shortName;

    @Column(name = Tables.Persons.TYPE)
    private Type type;

    @Column(name = Tables.Persons.CREATED_AT)
    private LocalDateTime createdAt;

    public enum Type {
        FAMILY, COMPOSER, CONDUCTOR, ACTOR, DIRECTOR, OTHER
    }
}
