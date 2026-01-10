package com.example;

import com.example.config.AppModule;
import com.example.ui.LoginForm;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppModule());

        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = injector.getInstance(LoginForm.class);
            loginForm.setVisible(true);
        });
    }
}
