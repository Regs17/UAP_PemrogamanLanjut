package org.example.model;

public class Barang {
    public String id;
    public String nama;
    public String kategori;
    public int stok;
    public double harga;
    public String tanggalMasuk; // <--- KITA UBAH JADI STRING

    public Barang(String id, String nama, String kategori, int stok, double harga, String tanggalMasuk) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.stok = stok;
        this.harga = harga;
        this.tanggalMasuk = tanggalMasuk;
    }

    // Format CSV: id,nama,kategori,stok,harga,tanggal
    public String toCSV() {
        return id + "," + nama + "," + kategori + "," + stok + "," + harga + "," + tanggalMasuk;
    }
}