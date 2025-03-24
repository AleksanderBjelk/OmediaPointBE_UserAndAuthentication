package mlm.praktik.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.*;
import mlm.praktik.entities.UserEntity;
import mlm.praktik.repositories.UserRepository;
import mlm.praktik.services.UserService;
import reactor.core.publisher.Mono;

import jakarta.inject.Inject;

@Controller("/users")
public class UserController {

    @Inject
    private UserRepository userRepository;
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserController (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Get("/{id}")
    @NonNull
    Mono<UserEntity> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Post("/create")
    public Mono<UserEntity> createUser(@Body UserEntity user) {
        return userRepository.save(user);
    }

    @Delete("/delete")
    public Mono<Long> deleteUser(String id) {
        return userRepository.deleteById(id);
    }
}

