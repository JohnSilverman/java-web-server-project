package http.response;

import java.io.DataOutputStream;
import java.io.OutputStream;

public interface HttpResponse {
    public HttpResponse status(int statusCode);
    public HttpResponse addHeader(ResponseHeader header);
    public HttpResponse body(ResponseBody body);
    public void send(OutputStream os);

    // 응답객체가 라우터에 도착하기 전에 중간에 있는 미들웨어들이 여기다 추가적인 정보 기록 ( ex : requested user login info )
    public void put(String key, Object data);
    public Object get(String key);

    enum Status {
        OK(200,"OK"),
        FOUND(302,"Found"),
        BAD_REQUEST(400,"Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        FORBIDDEN(403,"Forbidden"),
        NOT_FOUND(404,"Not Found"),
        METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
        INTERNAL_SERVER_ERROR(500,"Internal Server Error");

        private final int statusCode;
        private final String message;

        Status(int statusCode, String message){
            this.statusCode = statusCode;
            this.message = message;
        }

        public static Status of(int statusCode){
            for(var status : Status.values()){
                if(status.statusCode == statusCode) return status;
            }
            return null;
        }

        public String getMessage() { return this.message;}
        public int getStatusCode() {return this.statusCode;}
        public String toString(){ return String.valueOf(this.statusCode) + " " + this.message;}
    }
}
