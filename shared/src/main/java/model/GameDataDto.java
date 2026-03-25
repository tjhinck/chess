package model;

public record GameDataDto(
    int gameID,
    String gameName,
    String whiteUsername,
    String blackUsername) {

    private boolean notEmpty(String str){
        return (str != null && !str.isBlank());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(gameName);
        sb.append(" - White: ");
        sb.append(notEmpty(whiteUsername) ? whiteUsername : "open" );
        sb.append( " - Black: ");
        sb.append(notEmpty(blackUsername) ? blackUsername : "open" );
        return sb.toString();
    }
}
