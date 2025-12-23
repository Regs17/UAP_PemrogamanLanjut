package org.example.panel;

import org.example.AppFrame;
import org.example.controller.BarangController;
import org.example.model.Barang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;
import java.util.stream.Collectors;

public class LaporanPanel extends JPanel {

    private JLabel lblTotalJenis, lblTotalStok, lblTotalAset;
    private JPanel categoryContainer;

    // --- VARIABEL ANIMASI DENYUT ---
    private Timer animTimer;
    private float angle = 0.0f;

    public LaporanPanel(AppFrame frame) {
        setLayout(new GridBagLayout()); // Center Alignment
        setOpaque(false);

        BarangController.loadData();

        // --- 1. TIMER ANIMASI ---
        animTimer = new Timer(40, e -> {
            angle += 0.08f;
            if (angle > Math.PI * 200) angle = 0.0f;
            repaint();
        });

        // --- 2. KARTU UTAMA (UKURAN ASLI / FIXED) ---
        JPanel mainCard = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background Putih Transparan
                g2.setColor(new Color(255, 255, 255, 245));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        mainCard.setOpaque(false);
        // UKURAN KEMBALI SEPERTI CODINGAN ASLI KAMU
        mainCard.setPreferredSize(new Dimension(700, 600));
        mainCard.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- ISI KARTU ---
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 10, 0); // Spasi default
        c.gridx = 0; c.gridy = 0;
        c.weightx = 1.0; // Agar komponen di dalam melebar

        // 1. HEADER
        JLabel iconHeader = new JLabel("\uD83D\uDCCA"); // 📊
        iconHeader.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconHeader.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel title = new JLabel("Laporan & Statistik", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(50, 50, 50));

        mainCard.add(iconHeader, c);
        c.gridy++; c.insets = new Insets(0, 0, 25, 0);
        mainCard.add(title, c);

        // 2. RINGKASAN GLOBAL
        JPanel globalStats = new JPanel(new GridLayout(1, 3, 15, 0));
        globalStats.setOpaque(false);

        lblTotalJenis = createBigLabel();
        lblTotalStok = createBigLabel();
        lblTotalAset = createBigLabel();

        globalStats.add(createMiniStat("\uD83D\uDCDA Jenis", lblTotalJenis, new Color(59, 130, 246)));
        globalStats.add(createMiniStat("\uD83D\uDCE6 Stok", lblTotalStok, new Color(16, 185, 129)));
        globalStats.add(createMiniStat("\uD83D\uDCB5 Aset", lblTotalAset, new Color(245, 158, 11)));

        c.gridy++; c.insets = new Insets(0, 0, 25, 0);
        mainCard.add(globalStats, c);

        // 3. SUB-HEADER
        JLabel lblSub = new JLabel("Rincian Per Kategori Barang:");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSub.setForeground(Color.GRAY);
        c.gridy++; c.insets = new Insets(0, 0, 10, 0);
        mainCard.add(lblSub, c);

        // 4. DAFTAR KATEGORI
        categoryContainer = new JPanel();
        categoryContainer.setLayout(new BoxLayout(categoryContainer, BoxLayout.Y_AXIS));
        categoryContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(categoryContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.setPreferredSize(new Dimension(600, 150)); // Tinggi scroll area
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        c.gridy++; c.weighty = 1.0; c.fill = GridBagConstraints.BOTH;
        mainCard.add(scrollPane, c);

        // 5. TOMBOL AKSI
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setOpaque(false);

        GradientButton btnRefresh = new GradientButton("Refresh Data", "\uD83D\uDD04", new Color(14, 165, 233), new Color(56, 189, 248));
        GradientButton btnBack = new GradientButton("Ke Dashboard", "\uD83C\uDFE0", new Color(71, 85, 105), new Color(100, 116, 139));

        btnPanel.add(btnRefresh);
        btnPanel.add(btnBack);

        c.gridy++; c.weighty = 0; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(25, 0, 0, 0);
        mainCard.add(btnPanel, c);

        // Tambahkan Card ke Panel Utama (Otomatis Center karena GridBagLayout default)
        add(mainCard);

        // --- LOGIC ---
        updateStatistics();

        btnRefresh.addActionListener(e -> {
            BarangController.loadData();
            updateStatistics();
            JOptionPane.showMessageDialog(this, "✅ Data Laporan Diperbarui!");
        });
        btnBack.addActionListener(e -> frame.showDashboard());
    }

    // --- BACKGROUND RGB PULSE (PING-PONG) ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float pulseLeft = (float) (Math.sin(angle) + 1.0f) / 2.0f;
        float pulseRight = (float) (Math.cos(angle) + 1.0f) / 2.0f;

        // UNGU (KIRI)
        int r1 = (int) (40 + (60 * pulseLeft));
        int g1 = (int) (0 + (50 * pulseLeft));
        int b1 = (int) (150 + (105 * pulseLeft));
        Color colorLeft = new Color(r1, g1, b1);

        // PINK/MAGENTA (KANAN)
        int r2 = (int) (100 + (100 * pulseRight));
        int green2 = (int) (0 + (50 * pulseRight));
        int b2 = (int) (100 + (100 * pulseRight));
        Color colorRight = new Color(r2, green2, b2);

        GradientPaint gp = new GradientPaint(
                0, 0, colorLeft,
                getWidth(), getHeight(), colorRight
        );

        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    // --- HELPER LOGIC ---
    private void updateStatistics() {
        int totalItem = BarangController.listBarang.size();
        int totalStok = BarangController.listBarang.stream().mapToInt(b -> b.stok).sum();
        double totalAset = BarangController.listBarang.stream().mapToDouble(b -> b.stok * b.harga).sum();

        lblTotalJenis.setText(String.valueOf(totalItem));
        lblTotalStok.setText(String.valueOf(totalStok));
        lblTotalAset.setText(formatRupiahSingkat(totalAset));

        categoryContainer.removeAll();

        Map<String, Long> counts = BarangController.listBarang.stream()
                .collect(Collectors.groupingBy(b -> b.kategori, Collectors.counting()));

        Map<String, Double> values = BarangController.listBarang.stream()
                .collect(Collectors.groupingBy(b -> b.kategori, Collectors.summingDouble(b -> b.stok * b.harga)));

        if (counts.isEmpty()) {
            JLabel empty = new JLabel("Belum ada data barang.", JLabel.CENTER);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setBorder(new EmptyBorder(20,0,0,0));
            categoryContainer.add(empty);
        } else {
            for (String kat : counts.keySet()) {
                long count = counts.get(kat);
                double val = values.get(kat);
                categoryContainer.add(createCategoryRow(kat, count, val));
                categoryContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        categoryContainer.revalidate();
        categoryContainer.repaint();
    }

    private JPanel createCategoryRow(String kategori, long count, double value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240,240,240)),
                BorderFactory.createEmptyBorder(8, 5, 8, 5)
        ));
        row.setMaximumSize(new Dimension(1000, 50));

        JLabel lblName = new JLabel("\uD83C\uDFF7  " + kategori);
        lblName.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        lblName.setForeground(new Color(50, 50, 50));

        String infoText = String.format("<html><div style='text-align:right;'><b>%d Item</b><br/><span style='color:gray;font-size:11px;'>%s</span></div></html>",
                count, formatRupiah(value));
        JLabel lblInfo = new JLabel(infoText);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        row.add(lblName, BorderLayout.WEST);
        row.add(lblInfo, BorderLayout.EAST);
        return row;
    }

    private String formatRupiah(double val) {
        return "Rp " + String.format("%,.0f", val).replace(',', '.');
    }

    private String formatRupiahSingkat(double val) {
        if (val >= 1000000000) return String.format("%.1f M", val / 1000000000).replace('.', ',');
        if (val >= 1000000) return String.format("%.1f Jt", val / 1000000).replace('.', ',');
        return String.format("%,.0f", val).replace(',', '.');
    }

    private JLabel createBigLabel() {
        JLabel l = new JLabel("0");
        l.setFont(new Font("Segoe UI", Font.BOLD, 20));
        l.setForeground(new Color(33, 37, 41));
        return l;
    }

    private JPanel createMiniStat(String title, JLabel val, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230,230,230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        t.setForeground(color);

        p.add(t, BorderLayout.NORTH);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    // --- LIFECYCLE ---
    @Override
    public void addNotify() { super.addNotify(); if (animTimer != null) animTimer.start(); }
    @Override
    public void removeNotify() { super.removeNotify(); if (animTimer != null) animTimer.stop(); }

    class GradientButton extends JButton {
        private Color c1, c2, cur;
        public GradientButton(String t, String i, Color c1, Color c2) {
            super(i + " " + t); this.c1=c1; this.c2=c2; this.cur=c1;
            setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
            setForeground(Color.WHITE); setFocusPainted(false); setContentAreaFilled(false);
            setBorderPainted(false); setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(170, 45));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { cur=c1.brighter(); repaint(); }
                public void mouseExited(MouseEvent e) { cur=c1; repaint(); }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, cur, getWidth(), 0, c2));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
        }
    }
}