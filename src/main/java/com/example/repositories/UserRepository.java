package com.example.repositories;

import java.util.List;

import com.example.entites.User;
import com.example.repositories.interfaces.IUserRepository;
import com.example.util.HibernateManager;
import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserRepository implements IUserRepository {

    private final HibernateManager hibernateManager;

    @Inject
    public UserRepository(HibernateManager hibernateManager) {
        this.hibernateManager = hibernateManager;
    }

    @Override
    public void add(User user) {
        Transaction transaction = null;
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        Transaction transaction = null;
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                System.out.println("User is deleted");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(int id) {
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User findByNameAndPassword(String name, String password) {
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            return session.createQuery("from User where name = :name and password = :password", User.class)
                    .setParameter("name", name)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User findByName(String name) {
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            return session.createQuery("from User where name = :name", User.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
