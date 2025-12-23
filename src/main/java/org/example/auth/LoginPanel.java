package org.example.auth;

import org.example.AppFrame;
import org.example.controller.UserController;
// import org.example.util.SecurityUtil; <--- INI DIHAPUS, GAK PERLU LAGI

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JPanel {

    public JTextField usernameField;
    public JPasswordField passwordField;
    public JButton loginBtn, registerBtn;
    public JButton togglePasswordBtn;
    public boolean showPassword = false;
    private AppFrame frame;

    public LoginPanel(AppFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setOpaque(false);

        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        card.setPreferredSize(new Dimension(420, 380));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 0, 8, 0);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("LOGIN USER", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(50, 50, 50));

        JLabel labelUser = new JLabel("Username");
        labelUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelUser.setForeground(Color.GRAY);
        usernameField = new JTextField();
        styleField(usernameField);
        JPanel userPanel = new JPanel(new BorderLayout(10, 0));
        userPanel.setOpaque(false);
        userPanel.add(new JLabel("\uD83D\uDC64"), BorderLayout.WEST);
        userPanel.add(usernameField, BorderLayout.CENTER);

        JLabel labelPass = new JLabel("Password");
        labelPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelPass.setForeground(Color.GRAY);
        passwordField = new JPasswordField();
        styleField(passwordField);
        passwordField.setEchoChar('●');

        togglePasswordBtn = new JButton("\uD83D\uDC41");
        togglePasswordBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        togglePasswordBtn.setBorder(null);
        togglePasswordBtn.setContentAreaFilled(false);
        togglePasswordBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        togglePasswordBtn.addActionListener(e -> {
            showPassword = !showPassword;
            if (showPassword) {
                passwordField.setEchoChar((char) 0);
                togglePasswordBtn.setText("\uD83D\uDEAB");
            } else {
                passwordField.setEchoChar('●');
                togglePasswordBtn.setText("\uD83D\uDC41");
            }
        });

        JPanel passPanel = new JPanel(new BorderLayout(10, 0));
        passPanel.setOpaque(false);
        passPanel.add(new JLabel("\uD83D\uDD12"), BorderLayout.WEST);
        passPanel.add(passwordField, BorderLayout.CENTER);
        passPanel.add(togglePasswordBtn, BorderLayout.EAST);

        loginBtn = new JButton("Masuk");
        registerBtn = new JButton("Daftar");
        styleButton(loginBtn, new Color(79, 70, 229), new Color(116, 100, 242));
        styleButton(registerBtn, new Color(34, 197, 94), new Color(74, 222, 128));

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; card.add(title, c);
        c.gridy++; c.insets = new Insets(20, 0, 5, 0); card.add(labelUser, c);
        c.gridy++; c.insets = new Insets(0, 0, 10, 0); card.add(userPanel, c);
        c.gridy++; c.insets = new Insets(5, 0, 5, 0); card.add(labelPass, c);
        c.gridy++; c.insets = new Insets(0, 0, 20, 0); card.add(passPanel, c);
        c.gridy++; c.gridwidth = 2; c.insets = new Insets(0, 0, 10, 0); card.add(loginBtn, c);
        c.gridy++; card.add(registerBtn, c);

        add(card);

        loginBtn.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());
        registerBtn.addActionListener(e -> new RegisterFrame().setVisible(true));
    }

    private void performLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi username dan password!");
            return;
        }

        // Memanggil SecurityUtil (karena satu paket, langsung panggil saja)
        String hashPass = SecurityUtil.hash(pass);

        if (UserController.login(user, hashPass)) {
            JOptionPane.showMessageDialog(this, "Login Berhasil!");
            frame.setLoggedInUser(user);
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, new Color(79, 70, 229), 0, getHeight(), new Color(116, 100, 242));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void styleField(JTextField f) {
        f.setPreferredSize(new Dimension(260, 40));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        f.setBackground(Color.WHITE);
    }

    private void styleButton(JButton b, Color c1, Color c2) {
        b.setBackground(c1);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(100, 45));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(c2); }
            public void mouseExited(MouseEvent e) { b.setBackground(c1); }
        });
    }
}