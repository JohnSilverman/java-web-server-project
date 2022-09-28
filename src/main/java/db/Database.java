package db;

import com.google.common.collect.Maps;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.LoginToken;
import model.Member;
import model.Memo;
import model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Maps.newHashMap();
    private static Map<String, User> tokenToUser = new HashMap<>();
    private static Map<String, LoginToken> userToToken = new HashMap<>();

    private static Map<String, Member> tokenToMember = new HashMap<>();

    private static Map<String, LoginToken> memberToToken = new HashMap<>();

    public static void addUser(User user) {
        //users.put(user.getUserId(), user);
        persist(Member.fromUser(user));
    }

    public static LoginToken setLoginToken(User user){
//        LoginToken token = new LoginToken();
//        tokenToUser.put(token.getToken(), user);
//        userToToken.put(user.getUserId(), token);
//        return token;
        return setMemberLoginToken(Member.fromUser(user));
    }

    public static User findUserByToken(String token){
        //return tokenToUser.get(token);
        return User.fromMember(findMemberByTokenString(token));
    }

    public static User findUserById(String userId) {
        //return users.get(userId);
        Member member = findMemberById(userId);
        return User.fromMember(member);
    }

    public static List<Member> findAll() {
        //return users.values();
        return findAllMembers();
    }

    // Member

    public static void persist(Object member) {
        PostgreSQL pg = PostgreSQL.getInstance();
        Session session = pg.getSession();
        session.beginTransaction();
        session.persist(member);
        session.getTransaction().commit();
        session.close();
    }

    public static List<Member> findAllMembers() {
        Session session = PostgreSQL.getInstance().getSession();
        CriteriaQuery<Member> query = session.getCriteriaBuilder().createQuery(Member.class);
        return session.createQuery(query).getResultList();
    }

    public static LoginToken setMemberLoginToken(Member member){
        LoginToken token = new LoginToken();
        tokenToMember.put(token.getToken(), member);
        memberToToken.put(member.getUserId(), token);
        return token;
    }

    public static Member findMemberByTokenString(String token){
        return tokenToMember.get(token);
    }

    public static Member findMemberById(String memberId){
        Session session = PostgreSQL.getInstance().getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> model = cq.from(Member.class);
        cq.where(cb.equal(model.get("id"), memberId));
        TypedQuery<Member> q = session.createQuery(cq);
        return q.getSingleResult();
    }

    public static List<Memo> findAllMemos(){
        Session session = PostgreSQL.getInstance().getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Memo> cq = cb.createQuery(Memo.class);
        Root<Memo> root = cq.from(Memo.class);
        cq.select(root);

        Query<Memo> q = session.createQuery(cq);
        List<Memo> results = q.getResultList();
        return results;
    }

}
