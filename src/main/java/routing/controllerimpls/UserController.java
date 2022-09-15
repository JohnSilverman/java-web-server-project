package routing.controllerimpls;

import http.middlewares.impls.BodyParser;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.SimpleHttpResponse;
import routing.Controller;

import java.util.Map;

public class UserController implements Controller {
    @Override
    public HttpResponse getResponse(HttpRequest request) {
        // POST BODY
        Map<String,String> requestBody = (Map<String, String>) request.get(BodyParser.KEY_BODY);
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");
        String name = requestBody.get("name");
        String email = requestBody.get("email");


        HttpResponse response = new SimpleHttpResponse();
        switch (request.getMethod()){
            case POST: break;
            default : response = SimpleHttpResponse.Common.response405();
        }

        return response;
    }
}
