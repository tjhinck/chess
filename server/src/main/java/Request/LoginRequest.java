package Request;

import java.util.Objects;

public record LoginRequest(
        String username,
        String password) {
    public LoginRequest{
        Objects.requireNonNull(username, "username required");
        Objects.requireNonNull(password, "password required");
    }
}
