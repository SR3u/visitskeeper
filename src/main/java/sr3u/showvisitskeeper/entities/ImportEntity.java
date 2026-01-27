package sr3u.showvisitskeeper.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.Tables;

import java.util.UUID;

@Entity
@Table(name = "__IMPORT")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ImportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = Tables.Persons.ID)
    private UUID id;

    @Column(name = "MD5")
    private String md5;

    @Column(name = "FILE")
    private String file;
}
