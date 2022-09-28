package model;

import util.Security;

public class LoginToken {
    private String id = null;
    public LoginToken(){
        this.id = Security.genRandomId();
    }

    public LoginToken(String token){
        this.id = token;
    }

    public boolean isNotNull(){ return this.id != null; }

    public String getToken() { return this.id; }

}
