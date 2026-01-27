package sr3u.showvisitskeeper.entities;

import jakarta.persistence.Index;
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
import java.util.UUID;

@Entity
@Table(name = Tables.Venue._TABLE_NAME, indexes = {
        @Index(name=Tables.Venue._TABLE_NAME+"_IDX_"+Tables.Venue.ID, columnList = Tables.Venue.ID, unique = true),
        @Index(name=Tables.Venue._TABLE_NAME+"_IDX_"+Tables.Venue.SHORT_NAME, columnList = Tables.Venue.SHORT_NAME)
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VenueEntity implements DbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = Tables.Venue.ID)
    private UUID id;

    @Column(name = Tables.Venue.SHORT_NAME)
    private String shortName;

    @Column(name = Tables.Venue.FULL_NAME)
    private String fullName;

    @Column(name = Tables.Venue.AVATAR_URL)
    private String avatarUrl;

    @Column(name = Tables.Venue.CREATED_AT)
    private LocalDateTime createdAt;
}