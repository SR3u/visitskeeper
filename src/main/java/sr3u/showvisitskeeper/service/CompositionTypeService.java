package sr3u.showvisitskeeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sr3u.showvisitskeeper.dto.smart.CompositionType;
import sr3u.showvisitskeeper.dto.smart.annotations.RepositoryHolder;
import sr3u.showvisitskeeper.exceptions.NotFoundException;
import sr3u.showvisitskeeper.repo.repositories.CompositionTypeRepository;
import sr3u.showvisitskeeper.repo.service.CompositionTypeRepositoryService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompositionTypeService {
    private final CompositionTypeRepository compositionRepository;

    public CompositionType compositionTypeInfo(UUID id) {
        return RepositoryHolder.INSTANCE.getMapper().toCompositionType(compositionRepository.findById(id).orElseThrow(NotFoundException::new));
    }
}
