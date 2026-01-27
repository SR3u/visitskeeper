package sr3u.showvisitskeeper.dto.smart.annotations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr3u.showvisitskeeper.repo.service.BaseRepositoryService;
import sr3u.showvisitskeeper.repo.service.CompositionRepositoryService;
import sr3u.showvisitskeeper.repo.service.CompositionTypeRepositoryService;

import jakarta.annotation.PostConstruct;
import sr3u.showvisitskeeper.repo.service.PersonRepositoryService;
import sr3u.showvisitskeeper.repo.service.ProductionRepositoryService;
import sr3u.showvisitskeeper.repo.service.VenueRepositoryService;
import sr3u.showvisitskeeper.repo.service.VisitRepositoryService;

@Component
@RequiredArgsConstructor
@Getter
public class RepositoryHolder {
    public static RepositoryHolder INSTANCE;

    @Autowired
    CompositionRepositoryService compositionRepository;
    @Autowired
    CompositionTypeRepositoryService compositionTypeRepository;
    @Autowired
    PersonRepositoryService personRepository;
    @Autowired
    VenueRepositoryService venueRepository;
    @Autowired
    VisitRepositoryService visitRepository;
    @Autowired
    ProductionRepositoryService productionsRepository;

    @Autowired
    Mapper mapper;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

}
