package db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.*;

public class PostgreSQL {
    private static PostgreSQL pg = new PostgreSQL();

    private static SessionFactory sessionFactory;

    public static PostgreSQL getInstance(){
        return pg;
    }

    private PostgreSQL(){
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    public Session getSession(){
        return sessionFactory.openSession();
    }

    public void commit(Session session){
        session.getTransaction().commit();
        session.close();
    }
}
