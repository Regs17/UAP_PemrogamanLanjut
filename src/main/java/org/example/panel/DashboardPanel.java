package org.example.panel;

import org.example.AppFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DashboardPanel extends JPanel {

    // Variabel Animasi
    private Timer animTimer;
    private float angle = 0.0f; // Sudut untuk gerakan gelombang

    public DashboardPanel(AppFrame frame, String username) {
        setLayout(new GridBagLayout());
        setOpaque(false); // Agar background custom tergambar

        // --- 1. SETUP TIMER ANIMASI (PURPLE FLOW) ---
        // Timer berjalan setiap 50ms
        animTimer = new Timer(50, e -> {
            angle += 0.03f; // Kecepatan animasi
            if (angle > Math.PI * 2) {
                angle = 0.0f;
            }
            repaint(); // Gambar ulang layar
        });

        // --- 2. KARTU KACA (GLASS EFFECT) ---
        JPanel mainCard = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Warna Putih dengan Transparansi (Alpha 230 dari 255)
                g2.setColor(new Color(255, 255, 255, 230));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        mainCard.setOpaque(false);
        mainCard.setPreferredSize(new Dimension(750, 500));
        mainCard.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 5, 0);
        c.gridx = 0; c.gridy = 0;

        // --- 3. HEADER USER ---
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        userPanel.setOpaque(false);

        JLabel iconUser = new JLabel("\uD83D\uDC64"); // 👤
        iconUser.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        // Ikon user ikut tema ungu gelap agar kontras
        iconUser.setForeground(new Color(109, 40, 217));

        String sapaan = "Guest";
        if(username != null && !username.isEmpty()) {
            sapaan = username.substring(0, 1).toUpperCase() + username.substring(1);
        }
        JLabel lblWelcome = new JLabel("Halo, " + sapaan + "!", JLabel.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblWelcome.setForeground(new Color(60, 60, 60));

        userPanel.add(iconUser);
        userPanel.add(lblWelcome);

        mainCard.add(userPanel, c);

        // --- 4. JUDUL ---
        c.gridy++;
        JLabel lblTitle = new JLabel("DASHBOARD UTAMA", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(40, 40, 40));
        mainCard.add(lblTitle, c);

        // --- 5. TANGGAL ---
        c.gridy++;
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        datePanel.setOpaque(false);
        JLabel iconDate = new JLabel("\uD83D\uDCC5");
        iconDate.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault()));
        JLabel textDate = new JLabel(today);
        textDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textDate.setForeground(Color.GRAY);

        datePanel.add(iconDate);
        datePanel.add(textDate);
        mainCard.add(datePanel, c);

        // --- 6. MENU GRID ---
        c.gridy++; c.weighty = 1.0; c.fill = GridBagConstraints.BOTH;

        JPanel menuGrid = new JPanel(new GridLayout(2, 2, 25, 25));
        menuGrid.setOpaque(false);
        menuGrid.setBorder(new EmptyBorder(25, 0, 10, 0));

        // --- PERBAIKAN WARNA TOMBOL DISINI ---
        // Menggunakan warna yang berbeda agar mudah dibedakan

        // 1. Data Barang -> BIRU (Info)
        JButton btnList = createButton("Data Barang", "\uD83D\uDCE6", new Color(59, 130, 246));

        // 2. Tambah Data -> HIJAU (Action/Create)
        JButton btnAdd = createButton("Tambah Data", "\u2795", new Color(16, 185, 129));

        // 3. Laporan -> ORANYE (Analysis/Warning)
        JButton btnLaporan = createButton("Laporan", "\uD83D\uDCC8", new Color(245, 158, 11));

        // 4. Keluar -> MERAH (Danger/Exit)
        JButton btnLogout = createButton("Keluar", "\uD83D\uDEAA", new Color(239, 68, 68));

        btnList.addActionListener(e -> frame.changePanel(new ListPanel(frame)));
        btnAdd.addActionListener(e -> frame.changePanel(new FormPanel(frame)));
        btnLaporan.addActionListener(e -> frame.changePanel(new LaporanPanel(frame)));
        btnLogout.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Yakin keluar?", "Logout", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                frame.showLogin();
            }
        });

        menuGrid.add(btnList);
        menuGrid.add(btnAdd);
        menuGrid.add(btnLaporan);
        menuGrid.add(btnLogout);

        mainCard.add(menuGrid, c);
        add(mainCard);
    }

    // --- OTOMATISASI TIMER ---
    @Override
    public void addNotify() {
        super.addNotify();
        if (animTimer != null && !animTimer.isRunning()) animTimer.start();
    }
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (animTimer != null && animTimer.isRunning()) animTimer.stop();
    }

    // --- LOGIKA BACKGROUND ANIMASI (PURPLE FLOW) ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Algoritma warna Ungu - Pink (Tetap seperti request sebelumnya)
        float hue1 = 0.75f + (float) (Math.sin(angle) * 0.1f); // Bergerak sekitar Ungu
        float hue2 = 0.75f + (float) (Math.sin(angle + Math.PI) * 0.1f);

        Color color1 = Color.getHSBColor(hue1, 0.7f, 0.9f);
        Color color2 = Color.getHSBColor(hue2, 0.7f, 0.9f);

        GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    // Helper Tombol
    private JButton createButton(String text, String icon, Color color) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setBackground(color);
        iconPanel.setPreferredSize(new Dimension(65, 0));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblIcon.setForeground(Color.WHITE);
        iconPanel.add(lblIcon);

        JLabel lblText = new JLabel(text, SwingConstants.CENTER);
        lblText.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblText.setForeground(new Color(60, 60, 60));

        btn.add(iconPanel, BorderLayout.WEST);
        btn.add(lblText, BorderLayout.CENTER);
        btn.setBorder(BorderFactory.createLineBorder(new Color(230,230,230), 1));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(245,247,250)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(Color.WHITE); }
        });

        return btn;
    }
}