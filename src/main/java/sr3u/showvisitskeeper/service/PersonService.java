package sr3u.showvisitskeeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.smart.Person;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.exceptions.NotFoundException;
import sr3u.showvisitskeeper.repo.repositories.PersonRepository;
import sr3u.showvisitskeeper.repo.service.PersonRepositoryService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepositoryService personRepository;

    public Person personInfo(UUID id) {
        return RepositoryHolder.INSTANCE.getMapper().toPerson(personRepository.findById(id).orElseThrow(NotFoundException::new));
    }
}
