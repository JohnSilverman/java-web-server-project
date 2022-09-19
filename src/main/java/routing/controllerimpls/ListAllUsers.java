package routing.controllerimpls;

import com.google.gson.Gson;
import db.Database;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.SimpleHttpResponse;
import http.response.responsebody.JsonResponseBody;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.Controller;
import service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAllUsers implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(ListAllUsers.class);

    @Override
    public String getPattern() {
        return "/api/user/list";
    }

    @Override
    public HttpResponse getResponse(HttpRequest request) {
        HttpResponse response;
        if(request.getMethod() == HttpRequest.METHOD.GET){
            response = getUserListResponse(request);
        } else {
            response = SimpleHttpResponse.simpleResponse(401);
        }

        return response;
    }


    private HttpResponse getUserListResponse(HttpRequest request){
        HttpResponse response;
        try {
            //login check
            User user = UserService.getUserIfLogined(request);
            if(user == null) {
                return SimpleHttpResponse.simpleResponse(401);
            }

            //get data
            List<User> userList = UserService.queryUserList(Database.class);
            List<User> pwRemovedList = new ArrayList<>();

            for(User u : userList){
                User pwRemoved = new User(u.getUserId(),"",u.getName(),u.getEmail());
                pwRemovedList.add(pwRemoved);
            }

            //construct response object
            String jsonString = new Gson().toJson(pwRemovedList);
            response = new SimpleHttpResponse().status(200)
                    .body(new JsonResponseBody(jsonString));

        } catch (Exception e) {
            logger.error(e.toString());
            response = SimpleHttpResponse.simpleResponse(500);
        }

        return response;
    }
}
