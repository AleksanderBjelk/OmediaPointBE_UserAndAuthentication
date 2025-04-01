package mlm.praktik.services;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import mlm.praktik.entities.UserEntity;
import mlm.praktik.exceptions.MessageExceptionHandler;
import mlm.praktik.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Singleton
public class UserService {

    @Inject
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<UserEntity> findById(@NotBlank String id) {
        return userRepository.findById(id);
    }

    public Flux<UserEntity> findAll() {
        return userRepository.findAll()
                .doOnTerminate(() -> logger.info("All users fetched successfully"))
                .onErrorResume(ex -> {
                    logger.error("Failed to get all users", ex);
                    return Mono.error(new MessageExceptionHandler.DatabaseOperationException("Failed to fetch users: " + ex.getMessage()));
                });
    }

    public Mono<UserEntity> registerUser(UserEntity user) {
        return userRepository.save(user)
                .doOnTerminate(() -> logger.info("Creating user with name {}", user.getName()))
                .onErrorResume(ex -> {
                    logger.error("Failed to create user", ex); //Include the exception in the log
                    return Mono.error(new MessageExceptionHandler.DatabaseOperationException("Failed to create user: " + ex.getMessage()));
                });
    }

    public Mono<UserEntity> saveOrUpdateUser(UserEntity user) {
        logger.info("Attempting to save or update user with ID: {}", user.getId());

        return userRepository.findById(user.getId())
                .flatMap(existingUser -> {
                    //Existing user, update fields
                    logger.info("User found, updating details for user ID: {}", user.getId());
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPicture(user.getPicture());
                    existingUser.setRole(user.getRole() != null ? user.getRole() : "user");
                    existingUser.setApiToken(user.getApiToken() != null ? user.getApiToken() : "1234");
                    existingUser.setLastLoginAt(LocalDateTime.now());

                    return userRepository.update(existingUser)
                            .onErrorMap(ex -> new MessageExceptionHandler.DatabaseOperationException("Failed to update user: " + ex.getMessage()));
                })
                .switchIfEmpty(
                        //New user, explicitly set createdAt
                        userRepository.save(new UserEntity(
                                        user.getId(), user.getName(), user.getEmail(), user.getPicture(),
                                        "user", LocalDateTime.now(), "1234", LocalDateTime.now()))
                                .onErrorMap(ex -> {
                                    logger.error("Failed to save new user: {}", user.getId(), ex);
                                    return new MessageExceptionHandler.DatabaseOperationException("Failed to save user: " + ex.getMessage());
                                })
                )
                .doOnTerminate(() -> logger.info("User save or update process completed for user ID: {}", user.getId()));
    }


    public Mono<Long> deleteUser(String id) {
        return userRepository.deleteById(id)
                .doOnTerminate(() -> logger.info("User with ID {} deleted successfully", id))
                .onErrorResume(ex -> {
                    logger.error("Failed to delete user: {}", id, ex);
                    return Mono.error(new MessageExceptionHandler.DatabaseOperationException("Failed to delete user: " + ex.getMessage()));
                });
    }

    public Mono<UserEntity> updateUser(UserEntity user) {
        return userRepository.update(user)
                .doOnTerminate(() -> logger.info("User updated successfully"))
                .onErrorResume(ex -> {
                    logger.error("Failed to update user", ex);
                    return Mono.error(new MessageExceptionHandler.DatabaseOperationException("Failed to update user: " + ex.getMessage()));
                });
    }
}

