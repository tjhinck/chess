package request;

import java.util.Objects;

public record CreateGameRequest(
        String gameName) {
    public CreateGameRequest{
        Objects.requireNonNull(gameName);

        if (gameName.isBlank()) {
            throw new IllegalArgumentException("gameName cannot be blank");
        }
    }
}
