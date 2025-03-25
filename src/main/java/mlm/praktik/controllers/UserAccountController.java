package mlm.praktik.controllers;

import io.micronaut.http.annotation.*;
import mlm.praktik.entities.UserEntity;
import mlm.praktik.repositories.UserRepository;
import mlm.praktik.services.UserService;
import reactor.core.publisher.Mono;

import jakarta.inject.Inject;

@Controller()
public class UserAccountController {

    @Inject
    private UserService userService;

    public UserAccountController(UserService userService) {
        this.userService = userService;
    }

    @Delete("/user/delete")
    public Mono<Long> deleteUser(String id) {
        return userService.deleteUser(id);
    }

    @Put("/user/update")
    public Mono<UserEntity> updateUser(@Body UserEntity userEntity){
        return userService.updateUser(userEntity);
    }
}
