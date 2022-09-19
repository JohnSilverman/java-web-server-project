package routing.controllerimpls;

import db.Database;
import http.middlewares.impls.CookieManager;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.SimpleHttpResponse;
import model.LoginToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.Controller;
import service.UserService;

import static http.request.HttpRequest.METHOD.GET;
import static http.request.HttpRequest.METHOD.POST;

public class AuthController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final String pathPattern;

    public AuthController(String pathPattern){
        this.pathPattern = pathPattern;
    }

    @Override
    public HttpResponse getResponse(HttpRequest request) {
        String loginOrLogout = request.getParam("loginOrLogout");
        HttpResponse response;

        if(request.getMethod() == POST && loginOrLogout.equals("login")){
            response = login(request);
        } else if(request.getMethod() == GET && loginOrLogout.equals("logout")){
            response = logout(request);
        }
        else {
            response = SimpleHttpResponse.simpleResponse(405);
        }

        return response;
    }

    @Override
    public String getPattern() {
        return this.pathPattern;
    }

    private HttpResponse login(HttpRequest request){
        LoginToken token;
        try {
            token = UserService.login(request, Database.class);
        } catch (Exception e) {
            logger.error(e.toString());
            return SimpleHttpResponse.simpleResponse(500);
        }

        HttpResponse response = UserService.respondLoginReq(token);

        return response;
    }

    private HttpResponse logout(HttpRequest request){
        HttpResponse response = SimpleHttpResponse.redirect("/");
        CookieManager.setCookie(response,"token","");
        return response;
    }
}
