package routing.controllerimpls;

import http.request.HttpRequest;
import http.response.HttpResponse;
import routing.Controller;

public class UserController implements Controller {
    @Override
    public HttpResponse getResponse(HttpRequest request) {

        switch (request.getMethod()){
            case GET:break;
            case POST: break;
            case PUT: break;
            case DELETE: break;
        }

        return null;
    }
}
