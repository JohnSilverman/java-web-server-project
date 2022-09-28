package routing.controllerimpls;

import db.Database;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.SimpleHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.Controller;
import service.UserService;

import static http.request.HttpRequest.METHOD.POST;

public class UserSignup implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(UserSignup.class);

    @Override
    public String getPattern() {
        return "/api/user";
    }

    @Override
    public HttpResponse getResponse(HttpRequest request) {
        HttpResponse response;

        if(request.getMethod() == POST){
            response = postUser(request);
        } else {
            response = SimpleHttpResponse.simpleResponse(405);
        }

        return response;
    }

    private HttpResponse postUser(HttpRequest request){
        try {
            UserService.postUser(request);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return SimpleHttpResponse.redirect("/");
    }

}
