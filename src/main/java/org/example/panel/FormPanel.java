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
import java.time.LocalDate;
import java.util.Collections;
import java.util.ArrayList;

public class FormPanel extends JPanel {

    private JTextField txtId, txtNama, txtStok, txtHarga;
    private JComboBox<String> cmbKategori;
    private JButton btnSave, btnCancel;
    private Barang barangEdit = null;

    // --- VARIABEL ANIMASI DENYUT ---
    private Timer animTimer;
    private float angle = 0.0f;

    public FormPanel(AppFrame frame) {
        this(frame, null);
    }

    public FormPanel(AppFrame frame, Barang barang) {
        this.barangEdit = barang;

        setLayout(new GridBagLayout());
        setOpaque(false);

        // --- 1. TIMER ANIMASI (40ms) ---
        animTimer = new Timer(40, e -> {
            angle += 0.08f;
            if (angle > Math.PI * 200) angle = 0.0f;
            repaint();
        });

        // --- 2. KARTU FORM ---
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background Putih Transparan
                g2.setColor(new Color(255, 255, 255, 245));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setPreferredSize(new Dimension(500, 620));

        // --- HEADER ---
        String judulText = (barangEdit == null) ? "Input Barang Baru" : "Edit Data Barang";
        JLabel title = new JLabel(judulText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 30, 30));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("ID Barang akan dibuat otomatis berurutan");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        card.add(subtitle);
        card.add(Box.createVerticalStrut(20));

        // --- FORM INPUT ---

        // 1. ID Barang
        txtId = createTextField();
        card.add(createInputGroup("ID Barang (Otomatis)", "\uD83C\uDD94", txtId));
        card.add(Box.createVerticalStrut(12));

        // 2. Nama Barang
        txtNama = createTextField();
        card.add(createInputGroup("Nama Barang", "\uD83D\uDCE6", txtNama));
        card.add(Box.createVerticalStrut(12));

        // 3. Kategori
        String[] kategories = {"Elektronik", "Pakaian", "Makanan", "Minuman", "Perabot", "Lainnya"};
        cmbKategori = new JComboBox<>(kategories);
        cmbKategori.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbKategori.setBackground(Color.WHITE);
        cmbKategori.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cmbKategori.setPreferredSize(new Dimension(100, 30));
        ((JComponent) cmbKategori.getRenderer()).setBorder(new EmptyBorder(0,5,0,0));

        card.add(createInputGroup("Kategori", "\uD83C\uDFF7\uFE0F", cmbKategori));
        card.add(Box.createVerticalStrut(12));

        // 4. Stok Awal
        txtStok = createTextField();
        card.add(createInputGroup("Stok Awal", "\uD83D\uDCCA", txtStok));
        card.add(Box.createVerticalStrut(12));

        // 5. Harga Satuan
        txtHarga = createTextField();
        card.add(createInputGroup("Harga Satuan (Rp)", "\uD83D\uDCB0", txtHarga));

        card.add(Box.createVerticalStrut(30));

        // --- TOMBOL AKSI ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPanel.setMaximumSize(new Dimension(1000, 40));

        btnCancel = new JButton("Batal");
        btnSave = new JButton("Simpan Data");

        styleButton(btnCancel, new Color(240, 240, 240), Color.BLACK);
        styleButton(btnSave, new Color(109, 40, 217), Color.WHITE); // Ungu

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        card.add(btnPanel);

        add(card);

        // --- PANGGIL LOGIC DISINI ---
        setupFormLogic();

        btnSave.addActionListener(e -> performSave(frame));
        btnCancel.addActionListener(e -> frame.showDashboard());
    }

    // --- LOGIC GENERATE ID URUT (BRG-001, BRG-002...) ---
    private void setupFormLogic() {
        if (barangEdit != null) {
            // MODE EDIT
            txtId.setText(barangEdit.id);
            txtId.setEditable(false);
            txtId.setBackground(new Color(245, 245, 245));
            txtId.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
            txtNama.setText(barangEdit.nama);
            cmbKategori.setSelectedItem(barangEdit.kategori);
            txtStok.setText(String.valueOf(barangEdit.stok));
            txtHarga.setText(String.format("%.0f", barangEdit.harga));
            txtNama.requestFocus();
        } else {
            // MODE TAMBAH BARU (ID OTOMATIS)
            BarangController.loadData(); // Load data terbaru dulu!

            int maxId = 0;

            // Loop untuk mencari ID tertinggi yang formatnya BRG-angka
            if (BarangController.listBarang != null && !BarangController.listBarang.isEmpty()) {
                for (Barang b : BarangController.listBarang) {
                    if (b.id.startsWith("BRG-")) {
                        try {
                            // Ambil angka setelah "BRG-" (misal BRG-001 -> 1)
                            // substring(4) memotong 4 karakter pertama "BRG-"
                            String numberStr = b.id.substring(4);
                            int currentId = Integer.parseInt(numberStr);
                            if (currentId > maxId) {
                                maxId = currentId;
                            }
                        } catch (Exception e) {
                            // Abaikan ID yang formatnya aneh/bukan angka
                        }
                    }
                }
            }

            // Tambahkan 1 untuk ID baru
            int nextId = maxId + 1;

            // Format jadi 3 digit angka (001, 002, 010, dst)
            String newIdString = String.format("BRG-%03d", nextId);

            txtId.setText(newIdString);
            txtId.setEditable(false); // Tidak boleh diedit manual
            txtId.setBackground(new Color(245, 245, 245));
            txtId.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

            txtNama.requestFocus();
        }
    }

    // --- BACKGROUND RGB PULSE (PING-PONG) ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
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

    // --- HELPER COMPONENT ---
    private JPanel createInputGroup(String labelText, String iconEmoji, JComponent inputField) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        JLabel lbl = new JLabel(iconEmoji + "  " + labelText);
        lbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        lbl.setForeground(new Color(80, 80, 80));

        p.add(lbl, BorderLayout.NORTH);
        p.add(inputField, BorderLayout.CENTER);
        return p;
    }

    private JTextField createTextField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        f.setPreferredSize(new Dimension(200, 30));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return f;
    }

    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(110, 38));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { b.setBackground(bg); }
        });
    }

    // --- LIFECYCLE ---
    @Override
    public void addNotify() { super.addNotify(); if (animTimer != null) animTimer.start(); }
    @Override
    public void removeNotify() { super.removeNotify(); if (animTimer != null) animTimer.stop(); }

    private void performSave(AppFrame frame) {
        try {
            String id = txtId.getText();
            String nama = txtNama.getText();
            String kateg = (String) cmbKategori.getSelectedItem();
            if(nama.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama barang wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int stok = Integer.parseInt(txtStok.getText());
            double harga = Double.parseDouble(txtHarga.getText());
            String tgl = LocalDate.now().toString();
            Barang b = new Barang(id, nama, kateg, stok, harga, tgl);
            if (barangEdit == null) {
                BarangController.addBarang(b);
                JOptionPane.showMessageDialog(this, "✅ Data Berhasil Disimpan!");
            } else {
                BarangController.updateBarang(barangEdit.id, b);
                JOptionPane.showMessageDialog(this, "✅ Data Berhasil Diperbarui!");
            }
            frame.changePanel(new ListPanel(frame));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stok dan Harga harus angka!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}