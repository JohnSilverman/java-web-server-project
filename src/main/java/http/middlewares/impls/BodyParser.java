package http.middlewares.impls;

import http.MIME;
import http.middlewares.MiddleWare;
import http.request.HttpRequest;
import http.response.HttpResponse;
import util.HttpStringUtil;
import util.JsonConverter;

import java.util.HashMap;
import java.util.Map;

public class BodyParser implements MiddleWare {

    public static final String KEY_BODY = "_BODY";

    private static BodyParser instance = new BodyParser();

    private BodyParser(){}
    public static BodyParser getInstance(){ return instance; }

    @Override
    public HttpRequest processRequest(HttpRequest request) {
        Map<String,String> map = new HashMap<>();

        if(request.getMethod().equals(HttpRequest.METHOD.GET) ||
            request.getMethod().equals(HttpRequest.METHOD.DELETE)) {
            request.putAdditionalData(KEY_BODY, new HashMap<String,String>());
            return request;
        }

        if(request.getHeader("Content-Type").equals(MIME.FORM.getMime())){
            map = HttpStringUtil.parseQueryString(request.getBody());
        } else if(request.getHeader("Content-Type").equals(MIME.JSON.getMime())){
            map = JsonConverter.jsonStringToMap(request.getBody());
        }

        request.putAdditionalData(KEY_BODY, map);
        return request;
    }

    @Override
    public HttpResponse processResponse(HttpResponse response) {
        return response;
    }
}
