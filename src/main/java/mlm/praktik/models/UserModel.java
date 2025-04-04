package mlm.praktik.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class UserModel {

    public UserModel(String sub, String email, String name, String picture, String role) {
        this.sub = sub;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.role = role;
    }

    public UserModel() {
    }


    String sub;
    String email;
    String name;
    String picture;
    String role;


    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
