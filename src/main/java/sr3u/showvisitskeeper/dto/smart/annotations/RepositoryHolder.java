package sr3u.showvisitskeeper.dto.smart.annotations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.repo.CompositionRepository;
import sr3u.showvisitskeeper.repo.CompositionTypeRepository;
import sr3u.showvisitskeeper.repo.PersonRepository;
import sr3u.showvisitskeeper.repo.VenueRepository;
import sr3u.showvisitskeeper.repo.VisitRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Getter
public class RepositoryHolder {
    public static RepositoryHolder INSTANCE;

    CompositionRepository compositionRepository;
    CompositionTypeRepository compositionTypeRepository;
    PersonRepository personRepository;
    VenueRepository venueRepository;
    VisitRepository visitRepository;

    Mapper mapper;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

}
