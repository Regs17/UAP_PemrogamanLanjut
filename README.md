# 📦 Aplikasi Manajemen Inventaris Barang (Inventory Management System)

Aplikasi desktop berbasis **Java Swing** yang dirancang untuk mengelola data inventaris barang secara efisien. Aplikasi ini menonjolkan antarmuka pengguna (UI) yang modern dengan penerapan konsep **Glassmorphism** dan animasi latar belakang **RGB Ping-Pong Pulse** yang dinamis.

Sistem ini tidak memerlukan database eksternal (seperti MySQL) karena menggunakan sistem penyimpanan file **CSV**, sehingga portabel dan mudah dijalankan di mana saja.

![Tampilan Form Input](image_b2aded.png)
*(Tampilan Form Tambah Barang dengan Auto-ID)*

## ✨ Fitur Utama

### 1. 🔐 Autentikasi Keamanan (Auth)
* **Login & Register:** Sistem login multi-user.
* **Keamanan Password:** Password dienkripsi menggunakan hashing **MD5** sebelum disimpan ke `users.csv`.
* **Toggle Password:** Fitur "Lihat Password" (👁️) untuk kenyamanan pengguna.
* **Validasi:** Cek duplikasi username saat registrasi.

### 2. 🖥️ Dashboard Interaktif
* **User Greeting:** Menyapa pengguna berdasarkan nama login.
* **Animasi Background:** Efek gradasi warna "Purple Flow" yang bergerak halus.
* **Navigasi Cepat:** Akses mudah ke menu Data Barang, Tambah Data, Laporan, dan Logout.

### 3. 📦 Manajemen Inventaris (CRUD)
* **List Data Modern:** Tabel data dengan desain *zebra-striped*, header custom, dan efek glass.
* **Pencarian Real-time:** Cari barang berdasarkan nama atau kategori secara instan.
* **Form Input Cerdas:**
    * **Auto-Increment ID:** ID Barang dibuat otomatis (misal: `BRG-001`, `BRG-002`) dengan membaca ID terakhir di database.
    * Validasi input angka untuk stok dan harga.
* **Edit & Hapus:** Kemudahan memperbarui atau menghapus data inventaris.

### 4. 🎨 UI/UX Premium
* **Glassmorphism:** Panel transparan dengan efek kaca di atas background animasi.
* **RGB Ping-Pong Pulse:** Animasi background khusus pada menu List dan Input yang berdenyut bergantian antara warna Ungu (Kiri) dan Pink/Magenta (Kanan).
* **Custom Components:** Tombol dengan efek *hover*, rounded corners, dan font **Segoe UI Emoji** untuk ikon yang menarik.

### 5. 📊 Laporan & Statistik
* **Ringkasan Aset:** Menampilkan total jenis barang, total stok fisik, dan total nilai aset (Rupiah).
* **Rincian Kategori:** Breakdown jumlah item dan nilai per kategori barang.

## 🛠️ Teknologi & Arsitektur

* **Bahasa Pemrograman:** Java (JDK 17+)
* **GUI Library:** Java Swing & AWT (Graphics2D for Animations)
* **Penyimpanan Data:** File Handling (CSV)
    * `inventaris.csv`: Menyimpan data barang.
    * `users.csv`: Menyimpan data user dan password hash.
* **Design Pattern:** MVC (Model-View-Controller) Architecture.

## 📂 Struktur Proyek

Berikut adalah struktur folder proyek berdasarkan kode sumber:

![Struktur Folder Proyek](image_fe5403.png)

```text
src/main/java/org/example/
│
├── auth/                 # Menangani Keamanan & Login
│   ├── LoginPanel.java
│   ├── RegisterFrame.java
│   └── SecurityUtil.java (MD5 Hashing)
│
├── controller/           # Logika Bisnis & File I/O
│   ├── BarangController.java (CRUD CSV Barang)
│   └── UserController.java (CRUD CSV User)
│
├── model/                # Representasi Data
│   ├── Barang.java
│   └── User.java
│
├── panel/                # Tampilan (View) Utama
│   ├── DashboardPanel.java
│   ├── ListPanel.java    (Tabel Data)
│   ├── FormPanel.java    (Input/Edit Data)
│   └── LaporanPanel.java (Statistik)
│
├── AppFrame.java         # Frame Utama / Navigasi
└── Main.java             # Entry Point Aplikasi