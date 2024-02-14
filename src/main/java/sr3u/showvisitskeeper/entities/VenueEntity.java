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
@Table(name = Tables.Venue._TABLE_NAME)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class VenueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = Tables.Venue.ID)
    private UUID id;

    @Column(name = Tables.Venue.SHORT_NAME)
    private String shortName;

    @Column(name = Tables.Venue.CREATED_AT)
    private LocalDateTime createdAt;
}