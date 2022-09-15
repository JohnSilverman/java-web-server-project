package routing;

import http.middlewares.MiddleWare;
import http.request.HttpRequest;
import http.response.HttpResponse;

public interface Router {
    public void addMiddleware(MiddleWare middleWare);
    public void addController(Controller controller);
    public HttpResponse routeAndGetResponse(HttpRequest request);
}
