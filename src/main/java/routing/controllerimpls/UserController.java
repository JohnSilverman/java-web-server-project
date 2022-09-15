package routing.controllerimpls;

import db.Database;
import http.middlewares.impls.BodyParser;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.SimpleHttpResponse;
import model.User;
import routing.Controller;

import javax.xml.crypto.Data;
import java.util.Map;

public class UserController implements Controller {

    private final String pathPattern;

    public UserController(String pathPattern){
        this.pathPattern = pathPattern;
    }

    @Override
    public HttpResponse getResponse(HttpRequest request) {
        HttpResponse response;
        switch (request.getMethod()){
            case POST:
                response = postUser(request);
                break;
            case GET:
            case DELETE:
            case PUT:
            default : response = SimpleHttpResponse.simpleResponse(405);
        }
        return response;
    }

    @Override
    public String getPattern() {
        return this.pathPattern;
    }

    private HttpResponse postUser(HttpRequest request){
        // POST BODY
        Map<String,String> requestBody = (Map<String, String>) request.get(BodyParser.KEY_BODY);
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");
        String name = requestBody.get("name");
        String email = requestBody.get("email");

        User user = new User(userId, password, name, email);
        Database.addUser(user);

        return SimpleHttpResponse.redirect("/");
    }
}
