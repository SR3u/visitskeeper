package sr3u.showvisitskeeper.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.Tables;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = Tables.Production._TABLE_NAME, indexes = {
        @Index(name = Tables.Production._TABLE_NAME + "_IDX_" + Tables.Production.ID, columnList = Tables.Production.ID, unique = true),
        @Index(name = Tables.Production._TABLE_NAME + "_IDX_" + Tables.Production.COMPOSITION_ID, columnList = Tables.Production.COMPOSITION_ID)
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = Tables.Production.ID)
    UUID id;

    @Column(name = Tables.Production.COMPOSITION_ID)
    UUID compositionId;

    @Column(name = Tables.Production.NOTES)
    String notes;

    @Column(name = Tables.Production.AVATAR_URL)
    String avatarUrl;

    @ElementCollection
    @CollectionTable(name = "PRODUCTION_DIRECTORS",
            joinColumns = {
                    @JoinColumn(name = "PRODUCTION_ID", referencedColumnName = "ID"),
            },
            indexes = {
                    @Index(name = "PRODUCTION_DIRECTORS_IDX_PRODUCTION_ID", columnList = "PRODUCTION_ID"),
            }
    )
    Set<UUID> directorIds;
}
