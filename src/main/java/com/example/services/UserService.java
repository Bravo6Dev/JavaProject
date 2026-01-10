package com.example.services;

import java.util.List;
import java.util.regex.Pattern;

import com.example.entites.User;
import com.example.repositories.interfaces.IUserRepository;
import com.example.services.interfaces.IUserService;
import com.example.util.Result;
import com.google.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

public class UserService implements IUserService {

    private IUserRepository userRepository;

    @Inject
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Result<Void> add(User user) {
        Result<Boolean> validation = isValid(user);

        if (!validation.isSuccess()) {
            return Result.failure(validation.getMessage());
        }

        // Encrypt password
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        userRepository.add(user);
        return Result.success(null);
    }

    @Override
    public Result<Void> remove(int id) {
        userRepository.remove(id);
        return Result.success(null);
    }

    @Override
    public Result<Void> update(User user) {
        Result<Boolean> validation = isValid(user);
        if (!validation.isSuccess()) {
            return Result.failure(validation.getMessage());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // Encrypt password if it's being updated
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);
        }
        userRepository.update(user);
        return Result.success(null);
    }

    @Override
    public Result<User> getUserById(int id) {
        User user = userRepository.getUserById(id);
        return Result.success(user);
    }

    @Override
    public Result<List<User>> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        return Result.success(users);
    }

    @Override
    public Result<User> authenticate(String name, String password) {
        User user = userRepository.findByName(name);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return Result.success(user);
        }
        return Result.success(null);
    }

    private Result<Boolean> isValid(User user) {
        if (user == null) {
            return Result.failure("User cannot be null");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            return Result.failure("User name cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return Result.failure("Password cannot be empty");
        }

        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        if (user.getEmail() == null || !emailPattern.matcher(user.getEmail()).matches()) {
            return Result.failure("Invalid email format");
        }

        // Example phone pattern: starts with 09[1-4] followed by 7 digits
        Pattern phonePattern = Pattern.compile("^09[1-4]\\d{7}$");
        if (user.getPhone() == null || !phonePattern.matcher(user.getPhone()).matches()) {
            return Result.failure("Invalid phone number format");
        }

        return Result.success(true);
    }

}
