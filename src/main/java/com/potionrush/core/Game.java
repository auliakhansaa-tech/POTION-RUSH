package com.potionrush.core;

import com.potionrush.brewing.AddIngredientCommand;
import com.potionrush.brewing.Cauldron;
import com.potionrush.brewing.CauldronState;
import com.potionrush.brewing.CommandHistory;
import com.potionrush.brewing.Ingredient;
import com.potionrush.brewing.PotionType;
import com.potionrush.customer.Customer;
import com.potionrush.customer.CustomerFactory;
import com.potionrush.customer.CustomerState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Menyimpan semua isi permainan dan mengatur jalannya permainan
public class Game {
    public static final double DURASI_HARI = 150;
    public static final double JEDA_PETUALANG = 12;
    public static final int MAKS_PETUALANG = 3;
    public static final int TARGET_KOIN = 60;

    private final GameSession session = new GameSession();
    private final List<Customer> petualang = new ArrayList<>();
    private final List<Cauldron> kuali = List.of(new Cauldron(), new Cauldron());
    private final CommandHistory riwayat = new CommandHistory();
    private final Random acak = new Random();

    private double sisaWaktuHari = DURASI_HARI;
    private double waktuPetualangBerikutnya = 1;
    private boolean selesai = false;

    // Barang yang sedang dipegang pemain (hanya boleh satu)
    private Ingredient bahanDipegang = null;
    private PotionType ramuanDipegang = null;

    // Pesan singkat untuk ditampilkan di layar
    private String pesan = "Klik bahan, lalu klik kuali.";

    // ===================== GAME LOOP =====================

    // Dipanggil GameLoop setiap frame. dt = detik sejak frame sebelumnya.
    public void update(double dt) {
        if (selesai) {
            return;
        }

        // 1. Waktu hari berjalan
        sisaWaktuHari -= dt;
        if (sisaWaktuHari <= 0) {
            sisaWaktuHari = 0;
            selesai = true;
            if (session.getKoin() >= TARGET_KOIN) {
                pesan = "Hari selesai! Target tercapai.";
            } else {
                pesan = "Hari selesai. Target belum tercapai.";
            }
            return;
        }

        // 2. Petualang baru datang secara berkala
        waktuPetualangBerikutnya -= dt;
        if (waktuPetualangBerikutnya <= 0) {
            if (petualang.size() < MAKS_PETUALANG) {
                petualang.add(CustomerFactory.buatAcak(acak));
            }
            waktuPetualangBerikutnya = JEDA_PETUALANG;
        }

        // 3. Kesabaran berkurang, petualang yang pergi dihapus
        for (int i = petualang.size() - 1; i >= 0; i--) {
            Customer c = petualang.get(i);
            c.kurangiKesabaran(dt);
            if (c.getStatus() == CustomerState.PERGI) {
                petualang.remove(i);
                session.kurangiReputasi();
                pesan = c.getJenis() + " pergi karena terlalu lama menunggu.";
            }
        }

        // 4. Kuali yang sedang mendidih
        for (Cauldron k : kuali) {
            k.update(dt);
        }

        // 5. Cek kalah
        if (session.isKalah()) {
            selesai = true;
            pesan = "Reputasi habis. Permainan berakhir.";
        }
    }

    // ===================== AKSI PEMAIN =====================
    // Dipanggil dari tampilan (GameView) saat pemain mengklik sesuatu

    public void klikBahan(Ingredient bahan) {
        if (selesai) {
            return;
        }
        if (ramuanDipegang != null) {
            pesan = "Serahkan dulu ramuan yang sedang dipegang.";
            return;
        }
        bahanDipegang = bahan;
        pesan = "Memegang " + bahan + ". Klik kuali untuk memasukkannya.";
    }

    public void klikKuali(int nomor) {
        if (selesai) {
            return;
        }
        Cauldron k = kuali.get(nomor);

        // a. Sedang memegang bahan: masukkan ke kuali
        if (bahanDipegang != null) {
            riwayat.jalankan(new AddIngredientCommand(k, bahanDipegang));
            bahanDipegang = null;
            pesan = "Bahan masuk. Klik kuali lagi untuk mendidihkan.";
            return;
        }

        // b. Tangan kosong dan kuali sudah diisi: mulai mendidih
        if (k.getStatus() == CauldronState.DIISI) {
            k.mulaiMendidih();
            pesan = "Kuali " + (nomor + 1) + " mulai mendidih.";
            return;
        }

        // c. Tangan kosong dan ramuan siap: ambil ramuan
        if (k.getStatus() == CauldronState.SIAP && ramuanDipegang == null) {
            PotionType hasil = k.ambilRamuan();
            if (hasil == null) {
                pesan = "Ramuan gagal, urutan bahan salah. Ramuan dibuang.";
            } else {
                ramuanDipegang = hasil;
                pesan = "Memegang ramuan " + hasil + ". Klik petualang yang memesannya.";
            }
        }
    }

    public void klikPetualang(int nomor) {
        if (selesai || ramuanDipegang == null || nomor >= petualang.size()) {
            return;
        }
        Customer c = petualang.get(nomor);
        int bayaran = c.hitungBayaran();
        if (c.layani(ramuanDipegang)) {
            session.tambahKoin(bayaran);
            petualang.remove(nomor);
            ramuanDipegang = null;
            pesan = c.getJenis() + " membayar " + bayaran + " koin.";
        } else {
            pesan = "Ramuan salah! " + c.getJenis() + " memesan " + c.getPesanan() + ".";
        }
    }

    public void undo() {
        riwayat.undo();
        pesan = "Bahan terakhir dibatalkan.";
    }

    // ===================== GETTER =====================

    public GameSession getSession() {
        return session;
    }

    public List<Customer> getPetualang() {
        return petualang;
    }

    public List<Cauldron> getKuali() {
        return kuali;
    }

    public double getSisaWaktuHari() {
        return sisaWaktuHari;
    }

    public boolean isSelesai() {
        return selesai;
    }

    public Ingredient getBahanDipegang() {
        return bahanDipegang;
    }

    public PotionType getRamuanDipegang() {
        return ramuanDipegang;
    }

    public String getPesan() {
        return pesan;
    }
}