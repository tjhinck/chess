package request;

import java.util.Objects;

public record RegisterRequest(
        String username,
        String password,
        String email) {
    public RegisterRequest{
        Objects.requireNonNull(username, "username required");
        Objects.requireNonNull(password, "password required");
        Objects.requireNonNull(email, "email required");

        if (username.isBlank()) {
            throw new IllegalArgumentException("username cannot be blank");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("password cannot be blank");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("email cannot be blank");
        }
    }
}
