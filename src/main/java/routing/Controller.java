package routing;

import http.request.HttpRequest;
import http.response.HttpResponse;

public interface Controller {
    public HttpResponse getResponse(HttpRequest request);
}
