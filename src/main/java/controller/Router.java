package controller;

import controller.Controller;
import http.request.HttpRequest;
import http.response.HttpResponse;

public interface Router {
    public void addController(String path, Controller controller);
    public HttpResponse route(HttpRequest request);
}
