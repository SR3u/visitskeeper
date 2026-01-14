package sr3u.showvisitskeeper.entities;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import lombok.experimental.SuperBuilder;
import sr3u.showvisitskeeper.Tables;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = Tables.Visit._TABLE_NAME)
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

    @Column(name = Tables.Visit.DIRECTOR_ID)
    UUID directorId;

    @Column(name = Tables.Visit.COMPOSITION_ID)
    UUID compositionId;

    @Column(name = Tables.Visit.VENUE_ID)
    UUID venueId;

    @Column(name = Tables.Visit.TICKET_PRICE)
    BigDecimal ticketPrice;

    @Column(name = Tables.Visit.DETAILS)
    String details;

    @Column(name = Tables.Visit.P_HASH)
    String perceptionHash;


    @Override
    public String getShortName() {
        return getDate() + " " + getCompositionId();
    }
}
