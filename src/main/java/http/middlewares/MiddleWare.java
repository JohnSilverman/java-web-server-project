package http.middlewares;

import http.request.HttpRequest;
import http.response.HttpResponse;

public interface MiddleWare {
    public HttpRequest processRequest(HttpRequest request);
    public HttpResponse processResponse(HttpResponse response);
}
