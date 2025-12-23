package org.example.panel;

import org.example.AppFrame;
import org.example.controller.BarangController;
import org.example.model.Barang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ListPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private AppFrame frame;

    // --- VARIABEL ANIMASI DENYUT ---
    private Timer animTimer;
    private float angle = 0.0f; // Sudut untuk gelombang Sinus

    public ListPanel(AppFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setOpaque(false);

        // --- 1. TIMER ANIMASI (40ms) ---
        animTimer = new Timer(40, e -> {
            angle += 0.08f; // Kecepatan Denyut
            if (angle > Math.PI * 200) angle = 0.0f;
            repaint();
        });

        // --- 2. KARTU UTAMA (GLASS EFFECT) ---
        JPanel mainCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 245));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        mainCard.setOpaque(false);
        mainCard.setBorder(new EmptyBorder(25, 30, 25, 30));

        // KONFIGURASI UKURAN KARTU (Margin 45px)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(45, 45, 45, 45);

        // --- 3. HEADER & LOGO ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel title = new JLabel("📋 Daftar Inventaris Barang");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 40));

        // Panel Pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);

        searchField = new JTextField(20);
        styleField(searchField);

        // TOMBOL CARI & RESET
        JButton btnSearch = new JButton("🔍 Cari");
        styleSmallButton(btnSearch);

        JButton btnRefresh = new JButton("🔄 Reset");
        styleSmallButton(btnRefresh);

        searchPanel.add(new JLabel("Cari Barang: "));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        mainCard.add(topPanel, BorderLayout.NORTH);

        // --- 4. TABEL DATA ---
        String[] columns = {"ID", "Nama Barang", "Kategori", "Stok", "Harga Satuan", "Tanggal Masuk"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainCard.add(scrollPane, BorderLayout.CENTER);

        // --- 5. TOMBOL BAWAH ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        bottomPanel.setOpaque(false);

        GradientButton btnUpdate = new GradientButton("Update Data", "\u270F", new Color(245, 158, 11), new Color(251, 191, 36));
        GradientButton btnDelete = new GradientButton("Hapus Data", "\uD83D\uDDD1", new Color(239, 68, 68), new Color(248, 113, 113));
        GradientButton btnBack = new GradientButton("Kembali", "\uD83D\uDEAA", new Color(79, 70, 229), new Color(116, 100, 242));

        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnBack);

        mainCard.add(bottomPanel, BorderLayout.SOUTH);

        add(mainCard, gbc);

        // --- LOGIKA ---
        loadData("");

        btnSearch.addActionListener(e -> loadData(searchField.getText()));
        btnRefresh.addActionListener(e -> { searchField.setText(""); loadData(""); });

        // Logic Update
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih baris yang mau diedit!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String id = (String) table.getValueAt(row, 0);
            Barang b = BarangController.getBarangById(id);
            if (b != null) frame.changePanel(new FormPanel(frame, b));
        });

        // Logic Delete
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih baris yang mau dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String id = (String) table.getValueAt(row, 0);
            if(JOptionPane.showConfirmDialog(this, "Yakin hapus " + id + "?", "Hapus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                BarangController.deleteBarang(id);
                loadData("");
                JOptionPane.showMessageDialog(this, "Data Dihapus.");
            }
        });

        btnBack.addActionListener(e -> frame.showDashboard());
    }

    // --- LOGIKA DENYUT KIRI-KANAN (FIXED VARIABLES) ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // <--- DEFINISI PERTAMA (Graphics)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float pulseLeft = (float) (Math.sin(angle) + 1.0f) / 2.0f;
        float pulseRight = (float) (Math.cos(angle) + 1.0f) / 2.0f;

        // WARNA KIRI (UNGU)
        int r1 = (int) (40 + (60 * pulseLeft));
        int g1 = (int) (0 + (50 * pulseLeft));
        int b1 = (int) (150 + (105 * pulseLeft));
        Color colorLeft = new Color(r1, g1, b1);

        // WARNA KANAN (PINK/MAGENTA)
        int r2 = (int) (100 + (100 * pulseRight));
        int green2 = (int) (0 + (50 * pulseRight)); // <--- GANTI NAMA JADI green2 (JANGAN g2)
        int b2 = (int) (100 + (100 * pulseRight));
        Color colorRight = new Color(r2, green2, b2);

        GradientPaint gp = new GradientPaint(
                0, 0, colorLeft,
                getWidth(), getHeight(), colorRight
        );

        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    // --- LIFECYCLE ---
    @Override
    public void addNotify() { super.addNotify(); if (animTimer != null) animTimer.start(); }
    @Override
    public void removeNotify() { super.removeNotify(); if (animTimer != null) animTimer.stop(); }

    private void loadData(String keyword) {
        tableModel.setRowCount(0);
        BarangController.loadData();
        ArrayList<Barang> list = BarangController.listBarang;

        if (!keyword.isEmpty()) {
            list = (ArrayList<Barang>) list.stream()
                    .filter(b -> b.nama.toLowerCase().contains(keyword.toLowerCase()) ||
                            b.kategori.toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }

        for (Barang b : list) {
            tableModel.addRow(new Object[]{
                    b.id, b.nama, b.kategori, b.stok,
                    "Rp " + String.format("%,.0f", b.harga).replace(',', '.'), b.tanggalMasuk
            });
        }
    }

    // --- STYLING ---
    private void styleTable(JTable t) {
        t.setRowHeight(40);
        t.setShowGrid(true);
        t.setGridColor(new Color(240, 240, 240));
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = t.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(new Color(70, 70, 70));
        header.setPreferredSize(new Dimension(0, 45));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void styleField(JTextField f) {
        f.setPreferredSize(new Dimension(200, 35));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleSmallButton(JButton b) {
        b.setBackground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        b.setPreferredSize(new Dimension(100, 35));
        b.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(new Color(245, 245, 250)); }
            public void mouseExited(MouseEvent e) { b.setBackground(Color.WHITE); }
        });
    }

    class GradientButton extends JButton {
        private Color c1, c2, cur;
        public GradientButton(String t, String i, Color c1, Color c2) {
            super(i + "  " + t); this.c1=c1; this.c2=c2; this.cur=c1;
            setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
            setForeground(Color.WHITE); setFocusPainted(false); setContentAreaFilled(false);
            setBorderPainted(false); setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(160, 45));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { cur=c1.brighter(); repaint(); }
                public void mouseExited(MouseEvent e) { cur=c1; repaint(); }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, cur, getWidth(), 0, c2));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g);
        }
    }
}