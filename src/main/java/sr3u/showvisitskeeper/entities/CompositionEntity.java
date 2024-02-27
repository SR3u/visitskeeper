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
@Table(name = Tables.Composition._TABLE_NAME)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CompositionEntity implements DbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = Tables.Composition.ID)
    private UUID id;

    @Column(name = Tables.Composition.NAME)
    private String name;

    @Column(name = Tables.Composition.CREATED_AT)
    private LocalDateTime createdAt;

    @Column(name = Tables.Composition.TYPE)
    private UUID type;

    @Column(name = Tables.Composition.COMPOSER_ID)
    private UUID composerId;

    @Override
    public String getShortName() {
        return getName();
    }
}