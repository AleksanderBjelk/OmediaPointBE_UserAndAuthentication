package mlm.praktik.entities;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Serdeable
@MappedEntity("users")
@Getter
@Setter
public class UserEntity {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String picture;

    @NotBlank
    private String role;

    private LocalDateTime lastLoginAt;

    private String apiToken;

    @DateCreated
    private LocalDateTime createdAt;

    public UserEntity(String id, String name, String email, String picture, String role ,LocalDateTime lastLoginAt, String apiToken, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.lastLoginAt = lastLoginAt;
        this.apiToken = apiToken;
        this.createdAt = createdAt;
    }

    public UserEntity(String id, String name, String email, String picture, LocalDateTime now) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.lastLoginAt = now;
        this.id = id;
    }
}