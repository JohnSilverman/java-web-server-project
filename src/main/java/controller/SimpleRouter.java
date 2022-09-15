package controller;

import http.request.HttpRequest;
import http.response.SimpleHttpResponse;
import http.response.HttpResponse;
import http.response.ResponseHeader;
import http.response.responsebody.PlainTextResponseBody;

import java.util.Map;

public class SimpleRouter implements Router {

    public Map<String, Controller> controllerMap;

    @Override
    public void addController(String path, Controller controller) {
        controllerMap.put(path, controller);
    }

    @Override
    public HttpResponse route(HttpRequest request) {
        HttpResponse response = new SimpleHttpResponse();
        if(!controllerMap.containsKey(request.getPath())){
            response.status(404)
                    .addHeader(new ResponseHeader("Content-Type","text/plain"))
                    .body(new PlainTextResponseBody("404 Not Found"));
            return response;
        }

        Controller controller = controllerMap.get(request.getPath());
        return controller.getResponse();
    }
}
