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
@Table(name = Tables.ComnpositionType._TABLE_NAME)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CompositionTypeEntity implements DbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = Tables.ComnpositionType.ID)
    private UUID id;

    @Column(name = Tables.ComnpositionType.VALUE)
    private String value;

    @Column(name = Tables.ComnpositionType.CREATED_AT)
    private LocalDateTime createdAt;

    @Override
    public String getShortName() {
        return getValue();
    }
}
