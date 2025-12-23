package org.example;

import org.example.auth.LoginPanel;
import org.example.controller.UserController;
import org.example.panel.DashboardPanel;
import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {

    private JPanel gradientBackground;
    private String currentUser = "Guest"; // Menyimpan nama user yang login

    public AppFrame() {
        setTitle("Aplikasi Manajemen Inventaris");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        UserController.load(); // Load data user dari CSV

        // Background Gradient Global
        gradientBackground = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(79, 70, 229),
                        0, getHeight(), new Color(116, 100, 242)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        showLogin();
    }

    // Method dipanggil dari LoginPanel saat sukses login
    public void setLoggedInUser(String username) {
        this.currentUser = username;
        showDashboard();
    }

    public void showLogin() {
        this.currentUser = null;
        setContentPane(gradientBackground);
        gradientBackground.removeAll();
        gradientBackground.add(new LoginPanel(this));
        revalidate();
        repaint();
    }

    public void showDashboard() {
        // Kirim username ke DashboardPanel
        changePanel(new DashboardPanel(this, currentUser));
    }

    public void changePanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }
}