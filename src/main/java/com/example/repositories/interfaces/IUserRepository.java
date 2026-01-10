package com.example.repositories.interfaces;

import java.util.List;

import com.example.entites.User;

public interface IUserRepository {
    public void add(User user);

    public void remove(int id);

    public void update(User user);

    public User getUserById(int id);

    public List<User> getAllUsers();

    public User findByNameAndPassword(String name, String password);

    public User findByName(String name);
}
