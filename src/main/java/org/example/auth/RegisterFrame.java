package org.example.auth;

import org.example.controller.UserController;
// import org.example.util.SecurityUtil; <--- HAPUS INI JUGA

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField, confirmField;
    private JButton registerBtn, backBtn;

    public RegisterFrame() {
        setTitle("Daftar Akun");
        setSize(450, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(79, 70, 229), 0, getHeight(), new Color(116, 100, 242)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(380, 480));
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 5, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;

        JLabel title = new JLabel("REGISTRASI", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(50, 50, 50));
        card.add(title, c);

        c.gridy++; card.add(createLabel("Username"), c);
        c.gridy++; usernameField = createTextField(); card.add(usernameField, c);

        c.gridy++; card.add(createLabel("Password"), c);
        c.gridy++; passwordField = createPasswordField(); card.add(passwordField, c);

        c.gridy++; card.add(createLabel("Konfirmasi Password"), c);
        c.gridy++; confirmField = createPasswordField(); card.add(confirmField, c);

        registerBtn = createButton("Daftar", new Color(34, 197, 94));
        c.gridy++; c.insets = new Insets(20, 0, 10, 0); card.add(registerBtn, c);

        backBtn = createButton("Batal", new Color(239, 68, 68));
        c.gridy++; c.insets = new Insets(0, 0, 0, 0); card.add(backBtn, c);

        mainPanel.add(card);
        setContentPane(mainPanel);

        registerBtn.addActionListener(e -> performRegister());
        backBtn.addActionListener(e -> dispose());
    }

    private void performRegister() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        String conf = new String(confirmField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi semua data!"); return;
        }
        if (!pass.equals(conf)) {
            JOptionPane.showMessageDialog(this, "Password tidak sama!"); return;
        }

        // Memanggil SecurityUtil (otomatis kenal)
        String hashPass = SecurityUtil.hash(pass);

        if (UserController.register(user, hashPass)) {
            JOptionPane.showMessageDialog(this, "Berhasil! Silakan Login.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username sudah ada!");
        }
    }

    private JLabel createLabel(String t) { JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 12)); l.setForeground(Color.GRAY); return l; }
    private JTextField createTextField() { JTextField f = new JTextField(); styleField(f); return f; }
    private JPasswordField createPasswordField() { JPasswordField f = new JPasswordField(); styleField(f); return f; }
    private void styleField(JTextField f) { f.setPreferredSize(new Dimension(0, 35)); f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(5, 5, 5, 5))); f.setBackground(Color.WHITE); }
    private JButton createButton(String t, Color c) { JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE); b.setFont(new Font("Segoe UI", Font.BOLD, 14)); b.setFocusPainted(false); b.setPreferredSize(new Dimension(0, 40)); b.setCursor(new Cursor(Cursor.HAND_CURSOR)); return b; }
}