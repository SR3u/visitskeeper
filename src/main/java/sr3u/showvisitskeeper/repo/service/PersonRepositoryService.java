package sr3u.showvisitskeeper.repo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sr3u.showvisitskeeper.entities.PersonEntity;
import sr3u.showvisitskeeper.repo.repositories.PersonRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PersonRepositoryService implements BaseRepositoryService<PersonEntity> {

    @Autowired
    PersonRepository repository;

    public Collection<PersonEntity> findByShortName(String shortName) {
        return repository.findByShortName(shortName);
    }

    public Collection<PersonEntity> findByShortNameContaining(String shortName) {
        return repository.findByShortNameContaining(shortName);
    }

    public Collection<PersonEntity> findByFullNameContaining(String searchString) {
        return repository.findByFullNameContaining(searchString);
    }

    public PersonEntity saveAndFlush(PersonEntity person) {
        return repository.saveAndFlush(person);
    }

    @Override
    public Optional<PersonEntity> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<PersonEntity> findAllById(Iterable<UUID> ids) {
        return repository.findAllById(ids);
    }
}
