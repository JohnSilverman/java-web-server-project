package model;

import util.Security;

public class LoginToken {
    private String id;
    private User user;
    private boolean notNull = false;

    public LoginToken(){}

    public LoginToken(User user){
        this.user = user;
        this.id = Security.genRandomId();
        this.notNull = true;
    }

    public boolean isNotNull(){ return this.notNull; }

    public String getToken() { return this.id; }

    public User getUser() { return this.user; }
}
