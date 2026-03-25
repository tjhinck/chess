package exception;

import com.google.gson.Gson;
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
    }

    public ResponseException(HttpCode httpCode, String message) {
        super(message);
        code = httpCode.getCode();
    }

    public int code(){
        return code;
    }

    public String toJson(){
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    }
}
