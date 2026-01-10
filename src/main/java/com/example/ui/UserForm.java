package com.example.ui;

import com.example.entites.User;
import com.example.services.interfaces.IUserService;
import com.example.util.Result;
import com.google.inject.Inject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserForm extends JDialog {

    private final IUserService userService;
    private User user;

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> genderBox;
    private JButton saveButton;
    private JButton cancelButton;

    @Inject
    public UserForm(IUserService userService) {
        this.userService = userService;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("User Details");
        setSize(500, 550);
        setModal(true);
        setLocationRelativeTo(null);

        // Main Panel with padding and background
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(new Color(245, 245, 245));
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title
        JLabel titleLabel = new JLabel("User Information", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(50, 50, 50));
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);

        // Reset insets for fields
        gbc.insets = new Insets(5, 0, 2, 0);

        // Helper to add fields
        addLabelAndField(mainPanel, gbc, "Full Name:", nameField = createTextField());
        addLabelAndField(mainPanel, gbc, "Phone Number:", phoneField = createTextField());
        addLabelAndField(mainPanel, gbc, "Email Address:", emailField = createTextField());

        // Password
        gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(passLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(passwordField, gbc);

        // Gender
        gbc.insets = new Insets(5, 0, 2, 0);
        gbc.gridy++;
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(genderLabel, gbc);

        gbc.gridy++;
        String[] genders = { "M", "F" };
        genderBox = new JComboBox<>(genders);
        genderBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderBox.setBackground(Color.WHITE);
        gbc.insets = new Insets(0, 0, 25, 0);
        mainPanel.add(genderBox, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);

        saveButton = new JButton("Save User");
        styleButton(saveButton, new Color(60, 120, 216));
        saveButton.addActionListener(e -> saveUser());

        cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(150, 150, 150));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 30));
        return field;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 2, 0);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(label, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(field, gbc);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            nameField.setText(user.getName());
            phoneField.setText(user.getPhone());
            emailField.setText(user.getEmail());
            genderBox.setSelectedItem(String.valueOf(user.getGender()));
            saveButton.setText("Update User");
        } else {
            // Clear fields for new user
            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
            passwordField.setText("");
            genderBox.setSelectedIndex(0);
            saveButton.setText("Save User");
        }
    }

    private void saveUser() {
        if (user == null) {
            user = new User();
        }
        user.setName(nameField.getText());
        user.setPhone(phoneField.getText());
        user.setEmail(emailField.getText());
        user.setPassword(new String(passwordField.getPassword()));

        String genderStr = (String) genderBox.getSelectedItem();
        if (genderStr != null && !genderStr.isEmpty()) {
            user.setGender(genderStr.charAt(0));
        }

        try {
            Result<Void> result;
            if (user.getId() == 0) {
                result = userService.add(user);
            } else {
                result = userService.update(user);
            }

            if (result.isSuccess()) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving user: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
