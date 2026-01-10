package com.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.example.entites.User;
import com.google.inject.Singleton;

@Singleton
public class HibernateManager {
    private final SessionFactory sessionFactory;

    public HibernateManager() {
        try {
            this.sessionFactory = new Configuration().configure()
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
