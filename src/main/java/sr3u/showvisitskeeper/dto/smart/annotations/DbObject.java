package sr3u.showvisitskeeper.dto.smart.annotations;

import lombok.SneakyThrows;
import org.springframework.data.jpa.repository.JpaRepository;
import sr3u.streamz.functionals.Functionex;
import sr3u.streamz.functionals.Supplierex;
import sr3u.streamz.optionals.Optionalex;

import java.util.Optional;
import java.util.UUID;


public class DbObject<T> {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<T> value;

    private final Supplierex<Optional<T>> supplier;

    public DbObject(Supplierex<Optional<T>> supplier) {
        this.supplier = supplier;
    }

    @SuppressWarnings("unchecked")
    public DbObject(Supplierex<JpaRepository<?, UUID>> repoSupplier, Supplierex<UUID> idSupplier) {
        this(repoSupplier, idSupplier, i -> (T) i);
    }

    @SuppressWarnings("unchecked")
    public <O> DbObject(Supplierex<JpaRepository<?, UUID>> repoSupplier,
                        Supplierex<UUID> idSupplier,
                        Functionex<O, T> converter) {
        this(() -> Optionalex.ofOptional(repoSupplier.get().findById(idSupplier.get()))
                .map(o->converter.apply((O) o))
                .optional());
    }

    @SneakyThrows
    public Optional<T> getOptional() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }

    public T get() {
        return getOptional().orElseThrow(NullPointerException::new);
    }

}
