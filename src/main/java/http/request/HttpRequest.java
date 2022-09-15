package http.request;

import java.util.List;
import java.util.Map;

public interface HttpRequest {
    public static enum METHOD {
        GET("GET"), POST("POST"), DELETE("DELETE"), PUT("PUT");

        private final String value;

        METHOD(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }

        public static METHOD findMethodByValue(String methodValue){
            for(METHOD method : METHOD.values()){
                if(method.getValue().equals(methodValue)){
                    return method;
                }
            }
            return null;
        }
    };

    public static final String KEY_IP = "_IP";
    public static final String KEY_PORT = "_PORT";

    public Map<String,String> getHeaderMap();
    public METHOD getMethod();
    public String getPath();
    public Map<String,String> getParamMap();
    public String getParam(String name);
    public String getBody();

    // 요청객체가 컨트롤러에 도착하기 전에 중간에 있는 미들웨어들이 여기다 추가적인 정보 기록 ( ex : requested user login info )
    public void put(String key, Object data);
    public Object get(String key);

}
