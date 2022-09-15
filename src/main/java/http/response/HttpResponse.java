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
}
