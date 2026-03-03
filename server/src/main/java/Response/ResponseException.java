package Response;

import server.Server;
import java.util.Map;

public class ResponseException extends Exception {
    int code;

    public enum httpCode{
        badRequest(400),
        unauthorized(401),
        alreadyTaken(403);

        private final int code;
        httpCode(int code) { this.code = code; }
        public int getCode(){ return code; }
    }

    public ResponseException(httpCode httpCode, String message) {
        super(message);
        code = httpCode.getCode();
    }

    public int code(){
        return code;
    }

    public String toJson(){
        return Server.gson.toJson(Map.of("message", getMessage(), "status", code));
    }
}
