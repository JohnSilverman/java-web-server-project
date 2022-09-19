package http.middlewares.impls;

import http.middlewares.MiddleWare;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.ResponseHeader;

import java.util.*;

public class CookieManager implements MiddleWare {

    public static String KEY_COOKIE_MAP = "_COOKIE";

    private static CookieManager instance = new CookieManager();

    private CookieManager(){}

    public static CookieManager getInstance() { return instance; }

    @Override
    public HttpRequest processRequest(HttpRequest request) {
        if(request.getHeader("Cookie") != null){
            Map<String,String> cookieMap = new HashMap<>();
            for(String line : Arrays.asList(request.getHeader("Cookie").split(";"))){
                String[] splitted = line.split("=");
                String name = splitted[0].trim();
                String value = splitted.length == 2 ? splitted[1].trim() : "";
                cookieMap.put(name,value);
            }
            request.putAdditionalData(KEY_COOKIE_MAP, cookieMap);
        }
        return request;
    }

    @Override
    public HttpResponse processResponse(HttpResponse response) {
        return response;
    }

    public static void setCookie(HttpResponse response, String name, String value){
        response.addHeader(new ResponseHeader("Set-Cookie", name + "=" + value + "; Path=/"));
    }

    public static void setCookie(HttpResponse response, String name, String value, Date expires){
        response.addHeader(new ResponseHeader("Set-Cookie", name + "=" + value + "; Expires=" + expires.getTime() + "; Path=/"));
    }
}
