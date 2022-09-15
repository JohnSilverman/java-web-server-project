package http.middlewares.impls;

import http.middlewares.MiddleWare;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.ResponseHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SimpleeCookieParser implements MiddleWare {

    public static String KEY_COOKIE_MAP = "_COOKIE";

    private static SimpleeCookieParser instance = new SimpleeCookieParser();

    private SimpleeCookieParser(){}

    @Override
    public HttpRequest processRequest(HttpRequest request) {
        if(request.getHeader("Cookie") != null){
            request.put(KEY_COOKIE_MAP, new ArrayList<>(Arrays.asList(request.getHeader("Cookie").split(";"))));
        }
        return request;
    }

    @Override
    public HttpResponse processResponse(HttpResponse response) {
        return response;
    }

    public static void setCookie(HttpResponse response, String name, String value){
        response.addHeader(new ResponseHeader("Set-Cookie", name + "=" + value));
    }

    public static void setCookie(HttpResponse response, String name, String value, Date expires){
        response.addHeader(new ResponseHeader("Set-Cookie", name + "=" + value + "; Expires=" + expires.getTime()));
    }
}
