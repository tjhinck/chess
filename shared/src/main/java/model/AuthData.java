package model;

import java.util.Objects;

public record AuthData(
        String authToken,
        String username) {
    public AuthData{
        Objects.requireNonNull(authToken, "authToken required");
        Objects.requireNonNull(username, "username required");
    }
}
