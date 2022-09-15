package http.middlewares.impls;

import http.MIME;
import http.middlewares.MiddleWare;
import http.request.HttpRequest;
import http.response.HttpResponse;
import util.HttpStringUtil;

import java.util.Map;

public class BodyParser implements MiddleWare {

    public static final String KEY_BODY = "_BODY";

    private static BodyParser instance = new BodyParser();

    private BodyParser(){}
    public static BodyParser getInstance(){ return instance; }

    @Override
    public HttpRequest processRequest(HttpRequest request) {
        Map<String,String> map;

        if(request.getHeader("Content-Type").equals(MIME.FORM.getMime())){
            map = HttpStringUtil.parseQueryString(request.getBody());
        } else if(request.getHeader("Content-Type").equals(MIME.JSON.getMime())){
            map = HttpStringUtil.parseQueryString(request.getBody());
        } else return request; // json, form 아니면 그냥 통과

        request.put(KEY_BODY, map);
        return request;
    }

    @Override
    public HttpResponse processResponse(HttpResponse response) {
        return response;
    }
}