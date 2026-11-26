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
import com.potionrush.ui.DisplayName;

import java.util.ArrayList;
import java.util.Random;

public class GameWorld {

    public static final double DURASI_HARI = 150;
    public static final double JEDA_PETUALANG = 12;
    public static final int MAKS_PETUALANG = 3;

    private GameSession session = new GameSession();
    private ArrayList<Customer> daftarPetualang = new ArrayList<>();
    private ArrayList<Cauldron> daftarKuali = new ArrayList<>();
    private CommandHistory riwayat = new CommandHistory();
    private Random acak = new Random();

    // Pola State: game hanya berjalan saat layar MAIN
    private ScreenState layar = ScreenState.MENU;

    private double sisaWaktuHari = DURASI_HARI;
    private double waktuPetualangBerikutnya = 1;

    private int koinHariIni = 0;
    private int dilayaniHariIni = 0;
    private int pergiHariIni = 0;
    private String alasanGameOver = "";

    private Ingredient bahanDipegang = null;
    private PotionType ramuanDipegang = null;
    private String pesan = "";

    public GameWorld() {
        daftarKuali.add(new Cauldron());
        daftarKuali.add(new Cauldron());
    }

    // ===== Perpindahan layar =====

    public void mulai() {
        session = new GameSession();
        siapkanHari();
        layar = ScreenState.MAIN;
    }

    public void lanjutHari() {
        if (layar != ScreenState.RINGKASAN) {
            return;
        }
        session.lanjutHari();
        siapkanHari();
        layar = ScreenState.MAIN;
    }

    public void keMenu() {
        layar = ScreenState.MENU;
    }

    // Mengosongkan toko untuk hari baru. Total koin dan reputasi tidak direset.
    private void siapkanHari() {
        daftarPetualang.clear();
        for (Cauldron kuali : daftarKuali) {
            kuali.reset();
        }
        riwayat.hapusSemua();

        sisaWaktuHari = DURASI_HARI;
        waktuPetualangBerikutnya = 1;
        koinHariIni = 0;
        dilayaniHariIni = 0;
        pergiHariIni = 0;
        bahanDipegang = null;
        ramuanDipegang = null;
        pesan = "Day " + session.getHari() + " begins. Target: " + getTargetHari() + " coins.";
    }

    // Hari 1 targetnya 80 koin, lalu naik 20 setiap hari
    public int getTargetHari() {
        return 80 + 20 * (session.getHari() - 1);
    }

    // ===== Game loop =====

    // Dipanggil GameLoop setiap frame. dt = jumlah detik sejak frame sebelumnya.
    public void update(double dt) {
        if (layar != ScreenState.MAIN) {
            return;
        }

        kurangiWaktuHari(dt);
        if (layar != ScreenState.MAIN) {
            return;
        }

        datangkanPetualang(dt);
        kurangiKesabaranPetualang(dt);
        updateKuali(dt);
        cekKalah();
    }

    private void kurangiWaktuHari(double dt) {
        sisaWaktuHari = sisaWaktuHari - dt;

        if (sisaWaktuHari <= 0) {
            sisaWaktuHari = 0;
            akhiriHari();
        }
    }

    private void datangkanPetualang(double dt) {
        waktuPetualangBerikutnya = waktuPetualangBerikutnya - dt;

        if (waktuPetualangBerikutnya <= 0) {
            if (daftarPetualang.size() < MAKS_PETUALANG) {
                Customer petualangBaru = CustomerFactory.buatAcak(acak, session.getHari());
                daftarPetualang.add(petualangBaru);
            }
            waktuPetualangBerikutnya = JEDA_PETUALANG;
        }
    }

    private void kurangiKesabaranPetualang(double dt) {
        // Dari belakang ke depan, supaya aman saat menghapus isi list
        for (int i = daftarPetualang.size() - 1; i >= 0; i--) {
            Customer petualang = daftarPetualang.get(i);
            petualang.kurangiKesabaran(dt);

            if (petualang.getStatus() == CustomerState.PERGI) {
                daftarPetualang.remove(i);
                session.kurangiReputasi();
                pergiHariIni++;
                pesan = "The " + petualang.getJenis() + " left after waiting too long.";
            }
        }
    }

    private void updateKuali(double dt) {
        for (Cauldron kuali : daftarKuali) {
            kuali.update(dt);
        }
    }

    private void cekKalah() {
        if (session.isKalah()) {
            alasanGameOver = "Your shop's reputation ran out.";
            layar = ScreenState.GAME_OVER;
        }
    }

    private void akhiriHari() {
        if (koinHariIni >= getTargetHari()) {
            layar = ScreenState.RINGKASAN;
        } else {
            alasanGameOver = "You didn't reach today's coin target.";
            layar = ScreenState.GAME_OVER;
        }
    }

    // ===== Aksi pemain (dipanggil saat layar diklik) =====

    public void klikBahan(Ingredient bahan) {
        if (layar != ScreenState.MAIN) {
            return;
        }
        if (ramuanDipegang != null) {
            pesan = "Serve the potion you're holding first.";
            return;
        }
        bahanDipegang = bahan;
        pesan = "Holding " + DisplayName.bahan(bahan) + ". Click a cauldron to add it.";
    }

    public void klikKuali(int nomorKuali) {
        if (layar != ScreenState.MAIN) {
            return;
        }
        Cauldron kuali = daftarKuali.get(nomorKuali);

        if (bahanDipegang != null) {
            // Sedang memegang bahan: masukkan bahan ke kuali
            riwayat.jalankan(new AddIngredientCommand(kuali, bahanDipegang));
            bahanDipegang = null;
            pesan = "Ingredient added. Click the cauldron again to boil it.";

        } else if (kuali.getStatus() == CauldronState.DIISI) {
            // Tangan kosong dan kuali sudah ada isinya: mulai mendidih
            kuali.mulaiMendidih();
            pesan = "Cauldron " + (nomorKuali + 1) + " is boiling.";

        } else if (kuali.getStatus() == CauldronState.SIAP && ramuanDipegang == null) {
            // Ramuan sudah siap: ambil ramuannya
            PotionType hasil = kuali.ambilRamuan();
            if (hasil == null) {
                pesan = "The potion failed because the order was wrong. It was thrown away.";
            } else {
                ramuanDipegang = hasil;
                pesan = "Holding " + DisplayName.ramuan(hasil) + ". Click the adventurer who ordered it.";
            }
        }
    }

    public void klikPetualang(int nomorPetualang) {
        if (layar != ScreenState.MAIN) {
            return;
        }
        if (ramuanDipegang == null) {
            return;
        }
        if (nomorPetualang >= daftarPetualang.size()) {
            return;
        }

        Customer petualang = daftarPetualang.get(nomorPetualang);
        int bayaran = petualang.hitungBayaran();

        if (petualang.layani(ramuanDipegang)) {
            session.tambahKoin(bayaran);
            koinHariIni = koinHariIni + bayaran;
            dilayaniHariIni++;
            daftarPetualang.remove(nomorPetualang);
            ramuanDipegang = null;
            pesan = "The " + petualang.getJenis() + " paid " + bayaran + " coins.";
        } else {
            pesan = "Wrong potion! The " + petualang.getJenis() + " ordered " + DisplayName.ramuan(petualang.getPesanan()) + ".";
        }
    }

    public void undo() {
        if (layar != ScreenState.MAIN) {
            return;
        }

        boolean berhasil = riwayat.undo();
        if (berhasil) {
            pesan = "Last ingredient removed.";
        } else {
            pesan = "Nothing to undo.";
        }
    }

    // ===== Getter =====

    public ScreenState getLayar() {
        return layar;
    }

    public GameSession getSession() {
        return session;
    }

    public ArrayList<Customer> getPetualang() {
        return daftarPetualang;
    }

    public ArrayList<Cauldron> getKuali() {
        return daftarKuali;
    }

    public double getSisaWaktuHari() {
        return sisaWaktuHari;
    }

    public int getKoinHariIni() {
        return koinHariIni;
    }

    public int getDilayaniHariIni() {
        return dilayaniHariIni;
    }

    public int getPergiHariIni() {
        return pergiHariIni;
    }

    public String getAlasanGameOver() {
        return alasanGameOver;
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
