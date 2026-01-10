package com.example.ui;

import com.example.entites.User;
import com.example.services.interfaces.IUserService;
import com.example.util.Result;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class UserListForm extends JFrame {

    private final IUserService userService;
    private final Injector injector;
    private JTable userTable;
    private DefaultTableModel tableModel;

    @Inject
    public UserListForm(IUserService userService, Injector injector) {
        this.userService = userService;
        this.injector = injector;
        initializeUI();
        loadUsers();
    }

    private void initializeUI() {
        setTitle("User Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Background
        getContentPane().setBackground(new Color(245, 245, 245));

        // Title Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        topPanel.setBackground(new Color(245, 245, 245));
        JLabel titleLabel = new JLabel("All Users");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = { "ID", "Name", "Phone", "Email", "Gender" };

        // Prevent editing cells directly
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        styleTable(userTable);

        // Hide ID Column (Index 0)
        userTable.getColumnModel().removeColumn(userTable.getColumnModel().getColumn(0));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(new EmptyBorder(0, 20, 0, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBackground(new Color(245, 245, 245));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton addButton = createStyledButton("Add User", new Color(46, 204, 113));
        JButton editButton = createStyledButton("Edit User", new Color(52, 152, 219));
        JButton deleteButton = createStyledButton("Delete User", new Color(231, 76, 60));

        addButton.addActionListener(e -> openUserForm(null));
        editButton.addActionListener(e -> editUser());
        deleteButton.addActionListener(e -> deleteUser());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(100, 100, 100));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Center all cell contents
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        Result<List<User>> result = userService.getAllUsers();
        if (result.isSuccess() && result.getData() != null) {
            for (User user : result.getData()) {
                Object[] row = {
                        user.getId(),
                        user.getName(),
                        user.getPhone(),
                        user.getEmail(),
                        user.getGender()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void openUserForm(User user) {
        UserForm userForm = injector.getInstance(UserForm.class);
        userForm.setUser(user);
        userForm.setVisible(true);
        loadUsers(); // Refresh after dialog closes
    }

    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            // convertRowIndexToModel handles sorting (if we added it later)
            int modelRow = userTable.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0);

            Result<User> result = userService.getUserById(id);
            if (result.isSuccess() && result.getData() != null) {
                openUserForm(result.getData());
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = userTable.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                userService.remove(id);
                loadUsers();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }
}
