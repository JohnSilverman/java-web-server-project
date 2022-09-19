package db;

import com.google.common.collect.Maps;

import model.LoginToken;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Maps.newHashMap();
    private static Map<String, User> tokenToUser = new HashMap<>();
    private static Map<String, LoginToken> userToToken = new HashMap<>();

    // don't change method name
    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    // don't change method name
    public static LoginToken setLoginToken(User user){
        LoginToken token = new LoginToken(user);
        tokenToUser.put(token.getToken(), user);
        userToToken.put(user.getUserId(), token);
        return token;
    }

    // don't change method name
    public static User findUserByToken(String token){ return tokenToUser.get(token); }

    public static LoginToken findTokenByUser(User user) { return userToToken.get(user.getUserId()); }

    // don't change method name
    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
