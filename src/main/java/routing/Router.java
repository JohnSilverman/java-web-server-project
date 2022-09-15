package routing;

import http.middlewares.MiddleWare;
import http.request.HttpRequest;
import http.response.HttpResponse;

public interface Router {
    public void addMiddleware(MiddleWare middleWare);
    public void addController(String path, Controller controller);
    public HttpResponse routeAndGetResponse(HttpRequest request);
}
