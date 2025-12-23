package org.example.controller;

import org.example.model.Barang;
import java.io.*;
import java.util.ArrayList;

public class BarangController {

    public static ArrayList<Barang> listBarang = new ArrayList<>();

    private static final String FILE_NAME = "inventaris.csv";
    // DEFINISI HEADER AGAR RAPI
    private static final String CSV_HEADER = "ID,Nama Barang,Kategori,Stok,Harga,Tanggal Masuk";

    // --- LOAD DATA (BACA CSV) ---
    public static void loadData() {
        listBarang.clear();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // LOGIKA PENTING: SKIP HEADER
                // Jika baris ini adalah header/judul, jangan diproses sebagai barang
                if (line.trim().equalsIgnoreCase(CSV_HEADER) || line.toUpperCase().startsWith("ID,")) {
                    continue;
                }

                String[] data = line.split(",");
                if (data.length == 6) {
                    try {
                        Barang b = new Barang(
                                data[0], // ID
                                data[1], // Nama
                                data[2], // Kategori
                                Integer.parseInt(data[3]), // Stok
                                Double.parseDouble(data[4]), // Harga
                                data[5]  // Tanggal
                        );
                        listBarang.add(b);
                    } catch (NumberFormatException e) {
                        // Jika ada error parsing angka (misal baris rusak), abaikan saja
                        System.out.println("Skipping invalid row: " + line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- SAVE DATA (TULIS CSV) ---
    public static void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            // 1. TULIS HEADER DULU DI PALING ATAS
            bw.write(CSV_HEADER);
            bw.newLine();

            // 2. BARU TULIS DATA BARANG
            for (Barang b : listBarang) {
                bw.write(b.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- CRUD METHODS ---
    public static void addBarang(Barang b) {
        listBarang.add(b);
        saveData();
    }

    public static void deleteBarang(String id) {
        listBarang.removeIf(b -> b.id.equals(id));
        saveData();
    }

    public static Barang getBarangById(String id) {
        for (Barang b : listBarang) {
            if (b.id.equals(id)) return b;
        }
        return null;
    }

    public static void updateBarang(String idLama, Barang barangBaru) {
        for (int i = 0; i < listBarang.size(); i++) {
            if (listBarang.get(i).id.equals(idLama)) {
                listBarang.set(i, barangBaru);
                break;
            }
        }
        saveData();
    }
}