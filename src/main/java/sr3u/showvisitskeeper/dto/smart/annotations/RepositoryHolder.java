package sr3u.showvisitskeeper.dto.smart.annotations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.repo.CompositionRepository;
import sr3u.showvisitskeeper.repo.CompositionTypeRepository;
import sr3u.showvisitskeeper.repo.PersonRepository;
import sr3u.showvisitskeeper.repo.VenueRepository;
import sr3u.showvisitskeeper.repo.VisitRepository;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Getter
public class RepositoryHolder {
    public static RepositoryHolder INSTANCE;

    @Autowired
    CompositionRepository compositionRepository;
    @Autowired
    CompositionTypeRepository compositionTypeRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    VisitRepository visitRepository;

    @Autowired
    Mapper mapper;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

}
