package sr3u.showvisitskeeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.smart.Venue;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.exceptions.NotFoundException;
import sr3u.showvisitskeeper.repo.repositories.VenueRepository;
import sr3u.showvisitskeeper.repo.service.VenueRepositoryService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepositoryService venueRepository;

    public Venue venueInfo(UUID id) {
        return RepositoryHolder.INSTANCE.getMapper().toVenue(venueRepository.findById(id).orElseThrow(NotFoundException::new));
    }
}
