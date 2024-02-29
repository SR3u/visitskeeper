package sr3u.showvisitskeeper.dto.smart.annotations;

import lombok.SneakyThrows;
import org.springframework.data.jpa.repository.JpaRepository;
import sr3u.streamz.functionals.Functionex;
import sr3u.streamz.functionals.Supplierex;
import sr3u.streamz.optionals.Optionalex;
import sr3u.streamz.streams.Streamex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class DbList<T> {
    private List<T> value;

    private final Supplierex<List<T>> supplier;

    public DbList(Supplierex<List<T>> supplier) {
        this.supplier = supplier;
    }

    @SuppressWarnings("unchecked")
    public DbList(Supplierex<JpaRepository<?, UUID>> repoSupplier, Supplierex<Iterable<UUID>> idSupplier) {
        this(repoSupplier, idSupplier, i -> (T) i);
    }

    @SuppressWarnings("unchecked")
    public <O> DbList(Supplierex<JpaRepository<?, UUID>> repoSupplier,
                      Supplierex<Iterable<UUID>> idSupplier,
                      Functionex<O, T> converter) {
        this(() -> Streamex.ofCollection(repoSupplier.get().findAllById(idSupplier.get()))
                .map(o -> converter.apply((O) o))
                .collect(Collectors.toList()));
    }

    @SneakyThrows
    public List<T> get() {
        if (value == null) {
            value = supplier.get();
        }
        return value == null ? new ArrayList<>() : value;
    }

}
