package response;

import model.GameDataDto;

import java.util.Collection;

public record ListGamesResponse(Collection<GameDataDto> games) {
}
