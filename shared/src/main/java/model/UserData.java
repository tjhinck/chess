package model;

import java.util.Objects;

public record UserData(
        String username,
        String password,
        String email){
    public UserData{
        Objects.requireNonNull(username, "username required");
        Objects.requireNonNull(password, "password required");
        Objects.requireNonNull(email, "email required");
    }
}