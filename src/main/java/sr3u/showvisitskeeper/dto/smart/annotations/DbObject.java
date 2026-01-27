package sr3u.showvisitskeeper.dto.smart.annotations;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.SneakyThrows;
import org.springframework.data.jpa.repository.JpaRepository;
import sr3u.showvisitskeeper.repo.service.BaseRepositoryService;
import sr3u.streamz.functionals.Functionex;
import sr3u.streamz.functionals.Supplierex;
import sr3u.streamz.optionals.Optionalex;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@JsonSerialize(using = DbObjectSerializer.class)
public class DbObject<T> {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<T> value;
    private final AtomicBoolean loaded = new AtomicBoolean(false);

    private final Supplierex<Optional<T>> supplier;

    public DbObject(Supplierex<Optional<T>> supplier) {
        this.supplier = supplier;
    }

    @SuppressWarnings("unchecked")
    public DbObject(Supplierex<BaseRepositoryService<?>> repoSupplier, Supplierex<UUID> idSupplier) {
        this(repoSupplier, idSupplier, i -> (T) i);
    }

    @SuppressWarnings("unchecked")
    public <O> DbObject(Supplierex<BaseRepositoryService<?>> repoSupplier,
                        Supplierex<UUID> idSupplier,
                        Functionex<O, T> converter) {
        this(() -> {
            UUID id = idSupplier.get();
            if (id == null) {
                return Optional.empty();
            }
            return Optionalex.ofOptional(repoSupplier.get().findById(id))
                    .map(o -> converter.apply((O) o))
                    .optional();
        });
    }

    @SneakyThrows
    public Optional<T> getOptional() {
        if (!loaded.get()) {
            value = supplier.get();
            loaded.set(true);
        }
        return value;
    }

    public T get() {
        return getOptional().orElseThrow(NullPointerException::new);
    }

}
