package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Member {
    @Id
    private String userId;

    private String password;

    private String email;

    private String name;

    @Override
    public String toString() {
        return "Member [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    public static class Builder{
        String userId;
        String password;
        String email;
        String name;

        public Builder(){}

        public Builder setUserId(String id){
            this.userId = id;
            return this;
        }

        public Builder setPassword(String pw){
            this.password = pw;
            return this;
        }

        public Builder setEmail(String e){
            this.email = e;
            return this;
        }

        public Builder setName(String n){
            this.name = n;
            return this;
        }

        public Member build(){
            Member member = new Member();
            member.setEmail(this.email);
            member.setName(this.name);
            member.setPassword(this.password);
            member.setUserId(this.userId);
            return member;
        }
    }

    public static Member fromUser(User u){
        return new Member.Builder().setEmail(u.getEmail()).setName(u.getName()).setPassword(u.getPassword()).setUserId(u.getUserId()).build();
    }
}
