package request;

import java.util.Objects;

public record CreateGameRequest(String gameName) {
    public CreateGameRequest{
        Objects.requireNonNull(gameName);
    }
}
