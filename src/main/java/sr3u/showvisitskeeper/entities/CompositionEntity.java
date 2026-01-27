package sr3u.showvisitskeeper.entities;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.Tables;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = Tables.Composition._TABLE_NAME, indexes = {
        @Index(name=Tables.Composition._TABLE_NAME+"_IDX_"+Tables.Composition.ID, columnList = Tables.Composition.ID, unique = true),
        @Index(name=Tables.Composition._TABLE_NAME+"_IDX_"+Tables.Composition.NAME, columnList = Tables.Composition.NAME)
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CompositionEntity implements DbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = Tables.Composition.ID)
    private UUID id;

    @Column(name = Tables.Composition.NAME)
    private String name;

    @Column(name = Tables.Composition.CREATED_AT)
    private LocalDateTime createdAt;

    @Column(name = Tables.Composition.TYPE)
    private UUID typeId;

    //@Column(name = Tables.Composition.COMPOSER_ID)
    @ElementCollection
    private Set<UUID> composerIds;

    @Column(name = Tables.Composition.FULL_NAME)
    private String fullName;

    @Column(name = Tables.Composition.AVATAR_URL)
    private String avatarUrl;

    @Override
    public String getShortName() {
        return getName();
    }
}