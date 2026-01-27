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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.Tables;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = Tables.Visit._TABLE_NAME, indexes = {
        @Index(name=Tables.Visit._TABLE_NAME+"_IDX_"+Tables.Visit.ID, columnList = Tables.Visit.ID, unique = true),
        @Index(name=Tables.Visit._TABLE_NAME+"_IDX_"+Tables.Visit.P_HASH, columnList = Tables.Visit.P_HASH)
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class VisitEntity implements DbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = Tables.Visit.ID)
    UUID id;

    @Column(name = Tables.Visit.DATE)
    LocalDate date;

    @ElementCollection
    Set<UUID> attendeeIds;

    @ElementCollection
    Set<UUID> artistIds;

    @Column(name = Tables.Visit.CONDUCTOR_ID)
    UUID conductorId;

    //@Column(name = Tables.Visit.COMPOSITION_IDS)
    @ElementCollection
    Set<UUID> compositionIds;

    @ElementCollection
    Set<UUID> productionIds;

    @Column(name = Tables.Visit.VENUE_ID)
    UUID venueId;

    @Column(name = Tables.Visit.TICKET_PRICE)
    BigDecimal ticketPrice;

    @Column(name = Tables.Visit.NOTES)
    String notes;

    @Column(name = Tables.Visit.P_HASH, length = Tables.Visit.PHASH_LENGTH)
    String perceptionHash;


    @Override
    public String getShortName() {
        return getDate() + " " + getCompositionIds();
    }
}
