package response;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {
    int code;

    public enum HttpCode {
        badRequest(400),
        unauthorized(401),
        alreadyTaken(403),
        serverError(500);

        private final int code;

        HttpCode(int code) { this.code = code; }

        public int getCode(){ return code; }
        // Reverse lookup
        public static HttpCode fromCode(int code) {
            for (HttpCode status : HttpCode.values()) {
                if (status.code == code) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid HttpCode: " + code);
        }
    }

    public ResponseException(HttpCode status, String message) {
        super(message);
        code = status.getCode();
    }

    public int code(){
        return code;
    }

    public static ResponseException fromJson(String json){
        var map = new Gson().fromJson(json, HashMap.class);
        int code = ((Number) map.get("status")).intValue();
        HttpCode status = HttpCode.fromCode(code);
        String message = map.get("message").toString();
        return new ResponseException(status, message);
    }

    public String toJson(){
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    }
}
