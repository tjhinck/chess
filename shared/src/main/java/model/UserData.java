package model;

import java.util.Objects;

public record UserData(
        String username,
        String passwordHash,
        String email){
    public UserData{
        Objects.requireNonNull(username, "username required");
        Objects.requireNonNull(passwordHash, "passwordHash required");
        Objects.requireNonNull(email, "email required");
    }
}