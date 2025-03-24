package mlm.praktik.services;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import mlm.praktik.entities.UserEntity;
import mlm.praktik.exceptions.MessageExceptionHandler;
import mlm.praktik.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Singleton
public class UserService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
/*
    public Mono<UserEntity> createUser(@NotBlank String id,
                                       @NotBlank String name,
                                       @Email String email) {
        UserEntity newUser = new UserEntity();
        newUser.setId(id);
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setLastLoginAt(LocalDateTime.now());
        logger.info("Creating user with name {}", name);
        return userRepository.save(newUser);
    }

    public Mono<Void> deleteUser(String id) {
        return userRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        logger.warn("User with ID {} not found for deletion", id);
                        return Mono.error(new MessageExceptionHandler.NoDataException("User with ID " + id + " not found"));
                    }
                    return userRepository.deleteById(id)
                            .doOnTerminate(() -> logger.info("User with ID {} deleted successfully", id))
                            .onErrorMap(ex -> {
                                logger.error("Failed to delete user: {}", id, ex);
                                return new MessageExceptionHandler.DatabaseOperationException("Failed to delete user: " + ex.getMessage());
                            });
                }).then();
    }*/
}

