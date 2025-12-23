package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Menyiapkan tema UI agar lebih halus
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new AppFrame().setVisible(true));
    }
}