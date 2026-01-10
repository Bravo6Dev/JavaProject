package com.example.ui;

import com.example.entites.User;
import com.example.services.interfaces.IUserService;
import com.example.util.Result;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {

    private final IUserService userService;
    private final Injector injector;

    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    @Inject
    public LoginForm(IUserService userService, Injector injector) {
        this.userService = userService;
        this.injector = injector;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Login");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel with Padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(new Color(245, 245, 245)); // Light gray background
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title Label
        JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        mainPanel.add(titleLabel, gbc);

        // Username Label
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 5, 0); // Extra space before username
        JLabel nameLabel = new JLabel("Username");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(nameLabel, gbc);

        // Username Field
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(nameField, gbc);

        // Password Label
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(passLabel, gbc);

        // Password Field
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(60, 120, 216));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(this::authenticate);
        mainPanel.add(loginButton, gbc);

        // Allow Enter key to submit
        getRootPane().setDefaultButton(loginButton);
    }

    private void authenticate(ActionEvent e) {
        String name = nameField.getText();
        String password = new String(passwordField.getPassword());

        Result<User> result = userService.authenticate(name, password);
        if (result.isSuccess() && result.getData() != null) {
            openUserListForm();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openUserListForm() {
        UserListForm userListForm = injector.getInstance(UserListForm.class);
        userListForm.setVisible(true);
    }
}
