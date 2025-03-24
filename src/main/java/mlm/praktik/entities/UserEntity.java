package mlm.praktik.entities;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Serdeable
@MappedEntity("users")

public class UserEntity {

    @Id
    private String id;

    @NotBlank
    private String name;

    private String email;

    private String picture;

    @DateCreated
    private LocalDateTime lastLoginAt;

    public UserEntity(String id, String name, String email, String picture, LocalDateTime lastLoginAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.lastLoginAt = lastLoginAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }


}