package model;

public record GameDataDto(
    int gameID,
    String gameName,
    String whiteUsername,
    String blackUsername) {
}
