package com.example.services.interfaces;

import java.util.List;

import com.example.entites.User;
import com.example.util.Result;

public interface IUserService {
    Result<Void> add(User user);

    Result<Void> remove(int id);

    Result<Void> update(User user);

    Result<User> getUserById(int id);

    Result<List<User>> getAllUsers();

    Result<User> authenticate(String name, String password);
}
