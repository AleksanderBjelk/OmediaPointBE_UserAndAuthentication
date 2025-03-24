package mlm.praktik.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.validation.constraints.NotBlank;
import mlm.praktik.entities.UserEntity;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.MYSQL)
public interface UserRepository extends ReactorCrudRepository<UserEntity, String> {
    @NonNull
    Mono<UserEntity> findById(@NotBlank String id);
}


