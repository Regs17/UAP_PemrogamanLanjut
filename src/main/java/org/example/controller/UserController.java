package org.example.controller;

import org.example.model.User;
import java.io.*;
import java.util.ArrayList;

public class UserController {

    public static ArrayList<User> users = new ArrayList<>();
    private static final String FILE_NAME = "users.csv";

    // Load data dari CSV
    public static void load() {
        users.clear();
        File file = new File(FILE_NAME);

        // Jika file tidak ada, biarkan kosong (Jangan buat admin default)
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    // Masukkan ke list memori
                    users.add(new User(data[0], data[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Simpan data ke CSV
    public static void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User u : users) {
                bw.write(u.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Login (Cek Hash)
    public static boolean login(String username, String hashedPassword) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(hashedPassword)) {
                return true;
            }
        }
        return false;
    }

    // Register (Simpan Hash)
    public static boolean register(String username, String hashedPassword) {
        // Cek apakah username sudah ada
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return false; // Gagal: Username sudah terpakai
            }
        }

        // Tambahkan user baru ke memori
        users.add(new User(username, hashedPassword));

        // Langsung simpan ke file CSV
        saveUsers();

        return true;
    }
}