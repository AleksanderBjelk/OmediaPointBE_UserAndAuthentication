package mlm.praktik.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.*;
import mlm.praktik.entities.UserEntity;
import mlm.praktik.services.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.inject.Inject;

@Controller("/admin")
public class AdminController {

    @Inject
    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Get("/{id}")
    @NonNull
    public Mono<UserEntity> getUserById(String id) {
        return userService.findById(id);
    }

    @Get("/allUsers")
    public Flux<UserEntity> getAllUsers() {
        return userService.findAll();
    }

    @Post("/register")
    public Mono<UserEntity> registerUser(@Body UserEntity user) {
        return userService.registerUser(user);
    }
}
