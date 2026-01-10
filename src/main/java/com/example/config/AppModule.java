package com.example.config;

import com.example.repositories.interfaces.IUserRepository;
import com.example.services.UserService;
import com.example.services.interfaces.IUserService;
import com.example.repositories.UserRepository;
import com.example.util.HibernateManager;
import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IUserRepository.class).to(UserRepository.class);
        bind(IUserService.class).to(UserService.class);
        bind(HibernateManager.class).asEagerSingleton();
    }
}
