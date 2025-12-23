package org.example.auth;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JPanel {

    public JTextField usernameField;
    public JPasswordField passwordField;
    public JButton loginBtn, registerBtn;
    public JButton togglePasswordBtn;
    public boolean showPassword = false;

    public LoginFrame() {
        setLayout(new GridBagLayout());
        setOpaque(false); // Transparan agar background gradient AppFrame terlihat

        // Card panel putih
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        card.setPreferredSize(new Dimension(400, 320));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 10, 8, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("LOGIN SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(50, 50, 50));

        // Username
        usernameField = new JTextField();
        styleField(usernameField);

        // Password
        passwordField = new JPasswordField();
        styleField(passwordField);
        passwordField.setEchoChar('●');

        // Toggle Password
        togglePasswordBtn = new JButton("👁");
        togglePasswordBtn.setBorder(null);
        togglePasswordBtn.setContentAreaFilled(false);
        togglePasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        togglePasswordBtn.addActionListener(e -> {
            showPassword = !showPassword;
            passwordField.setEchoChar(showPassword ? (char) 0 : '●');
        });

        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.setOpaque(false);
        passPanel.add(passwordField, BorderLayout.CENTER);
        passPanel.add(togglePasswordBtn, BorderLayout.EAST);

        // Buttons
        loginBtn = new JButton("Masuk");
        registerBtn = new JButton("Daftar");
        styleButton(loginBtn, new Color(79, 70, 229));
        styleButton(registerBtn, new Color(16, 185, 129));

        // Layout Components
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        card.add(title, c);

        c.gridy++; c.gridwidth = 2;
        card.add(new JLabel("Username"), c);

        c.gridy++;
        card.add(usernameField, c);

        c.gridy++;
        card.add(new JLabel("Password"), c);

        c.gridy++;
        card.add(passPanel, c);

        c.gridy++; c.gridwidth = 1;
        card.add(loginBtn, c);

        c.gridx = 1;
        card.add(registerBtn, c);

        add(card);
    }

    private void styleField(JTextField f) {
        f.setPreferredSize(new Dimension(280, 40));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleButton(JButton b, Color color) {
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(130, 40));
        b.setBorderPainted(false);
    }
}