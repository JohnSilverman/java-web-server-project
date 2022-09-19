package service;

import db.Database;
import http.middlewares.impls.BodyParser;
import http.middlewares.impls.CookieManager;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.SimpleHttpResponse;
import model.LoginToken;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.controllerimpls.ListAllUsers;
import util.Security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public static User postUser(HttpRequest request, Class database) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //get body
        Map<String,String> requestBody = (Map<String, String>) request.getAdditionalData(BodyParser.KEY_BODY);
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");
        String name = requestBody.get("name");
        String email = requestBody.get("email");

        //extract method
        Method addUser = database.getMethod("addUser", User.class);

        //post
        String hashedPw = Security.encrypt(password);
        User user = new User(userId, hashedPw, name, email);
        addUser.invoke(null, user);

        return user;
    }

    public static LoginToken login(HttpRequest request, Class database) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String,String> requestBody = (Map<String, String>) request.getAdditionalData(BodyParser.KEY_BODY);
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");

        //extract method
        Method findUserById = database.getMethod("findUserById", String.class);
        Method setLoginToken = database.getMethod("setLoginToken", User.class);

        //check password
        String hashed = Security.encrypt(password);
        User user = (User)findUserById.invoke(null, userId);
        LoginToken token;
        if (user.getPassword().equals(hashed)){
            token = (LoginToken)setLoginToken.invoke(null, user);
        } else {
            token = new LoginToken();
        }

        return token;
    }

    public static HttpResponse respondLoginReq(LoginToken token){
        HttpResponse response;
        if (token.isNotNull()){
            response = SimpleHttpResponse.redirect("/");
            CookieManager.setCookie(response,"token",token.getToken());
        } else {
            response = SimpleHttpResponse.redirect("/user/login_failed.html");
            CookieManager.setCookie(response,"token","");
        }
        return response;
    }

    public static List<User> queryUserList(Class database) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method findAll = database.getMethod("findAll");
        return new ArrayList<>(((Collection<User>) findAll.invoke(null)));
    }

    public static User getUserIfLogined(HttpRequest request){
        String token = ((Map<String,String>)request.getAdditionalData(CookieManager.KEY_COOKIE_MAP)).get("token");
        return Database.findUserByToken(token); //nullable
    }
}
