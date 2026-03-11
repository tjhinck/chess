package request;

import java.util.Objects;

public record RegisterRequest(
        String username,
        String password,
        String email) {
    public RegisterRequest{
        Objects.requireNonNull(username, "username required");
        Objects.requireNonNull(password, "passwordHash required");
        Objects.requireNonNull(email, "email required");
    }
}
