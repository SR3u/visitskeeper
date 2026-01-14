package sr3u.showvisitskeeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.smart.Venue;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.exceptions.NotFoundException;
import sr3u.showvisitskeeper.repo.VenueRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;

    public Venue venueInfo(UUID id) {
        return RepositoryHolder.INSTANCE.getMapper().toVenue(venueRepository.findById(id).orElseThrow(NotFoundException::new));
    }
}
