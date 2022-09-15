package controller;

import http.request.HttpRequest;
import http.response.SimpleHttpResponse;
import http.response.HttpResponse;
import http.response.ResponseHeader;
import http.response.responsebody.PlainTextResponseBody;
import util.HttpStringUtil;

import java.util.Map;

public class SimpleRouter implements Router {

    public Map<String, Controller> controllerMap;

    @Override
    public void addController(String pathPrefix, Controller controller) {
        controllerMap.put(pathPrefix, controller);
    }

    @Override
    public HttpResponse route(HttpRequest request) {
        // 요청 path의 prefix에 매치되는 컨트롤러 찾기
        Controller controller = null;
        Map<String,String> pathVars = null;

        for(String pathPrefix : this.controllerMap.keySet()){
            pathVars = HttpStringUtil.matchPath(request.getPath(), pathPrefix);
            if(pathVars != null) {
                controller = this.controllerMap.get(pathPrefix);
                request.getParamMap().putAll(pathVars);
                break;
            }
        }

        return controller == null ? response404() : controller.getResponse(request);
    }


    private HttpResponse response404(){
        HttpResponse response = new SimpleHttpResponse();
        response.status(404)
                .addHeader(new ResponseHeader("Content-Type","text/plain"))
                .body(new PlainTextResponseBody("404 Not Found"));
        return response;
    }
}
