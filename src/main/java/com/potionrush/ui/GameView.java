package com.potionrush.ui;

import com.potionrush.brewing.Cauldron;
import com.potionrush.brewing.CauldronState;
import com.potionrush.brewing.Ingredient;
import com.potionrush.brewing.Recipe;
import com.potionrush.core.Game;
import com.potionrush.core.ScreenState;
import com.potionrush.customer.Customer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class GameView extends Pane {

    public static final int LEBAR = 1280;
    public static final int TINGGI = 720;

    // Posisi dan ukuran kotak petualang
    private static final double PETUALANG_X = 60;
    private static final double PETUALANG_Y = 90;
    private static final double PETUALANG_LEBAR = 220;
    private static final double PETUALANG_TINGGI = 170;
    private static final double PETUALANG_JARAK = 250;

    // Posisi dan ukuran kotak kuali
    private static final double KUALI_X = 120;
    private static final double KUALI_Y = 330;
    private static final double KUALI_LEBAR = 240;
    private static final double KUALI_TINGGI = 170;
    private static final double KUALI_JARAK = 300;

    // Posisi dan ukuran kotak bahan
    private static final double BAHAN_X = 60;
    private static final double BAHAN_Y = 560;
    private static final double BAHAN_LEBAR = 140;
    private static final double BAHAN_TINGGI = 80;
    private static final double BAHAN_JARAK = 160;

    // Posisi dan ukuran tombol undo
    private static final double UNDO_X = 880;
    private static final double UNDO_Y = 560;
    private static final double UNDO_LEBAR = 140;
    private static final double UNDO_TINGGI = 80;

    // Tombol di tengah layar, dipakai di layar MENU, RINGKASAN, dan GAME_OVER
    private static final double TOMBOL_X = 490;
    private static final double TOMBOL_Y = 500;
    private static final double TOMBOL_LEBAR = 300;
    private static final double TOMBOL_TINGGI = 80;

    private Game game;
    private Canvas canvas;
    private GraphicsContext g;

    public GameView(Game game) {
        this.game = game;
        canvas = new Canvas(LEBAR, TINGGI);
        g = canvas.getGraphicsContext2D();
        getChildren().add(canvas);

        // Setiap kali canvas diklik, panggil tanganiKlik dengan posisi mouse
        canvas.setOnMouseClicked(event -> tanganiKlik(event.getX(), event.getY()));
    }

    // =====================================================
    // KLIK MOUSE
    // =====================================================

    private void tanganiKlik(double klikX, double klikY) {
        ScreenState layar = game.getLayar();

        if (layar == ScreenState.MENU) {
            if (klikDiTombol(klikX, klikY)) {
                game.mulai();
            }
        } else if (layar == ScreenState.MAIN) {
            tanganiKlikSaatMain(klikX, klikY);
        } else if (layar == ScreenState.RINGKASAN) {
            if (klikDiTombol(klikX, klikY)) {
                game.lanjutHari();
            }
        } else if (layar == ScreenState.GAME_OVER) {
            if (klikDiTombol(klikX, klikY)) {
                game.keMenu();
            }
        }
    }

    private void tanganiKlikSaatMain(double klikX, double klikY) {
        // Cek apakah yang diklik petualang
        for (int i = 0; i < Game.MAKS_PETUALANG; i++) {
            double x = PETUALANG_X + i * PETUALANG_JARAK;
            if (klikDiDalamKotak(klikX, klikY, x, PETUALANG_Y, PETUALANG_LEBAR, PETUALANG_TINGGI)) {
                game.klikPetualang(i);
                return;
            }
        }

        // Cek apakah yang diklik kuali
        for (int i = 0; i < game.getKuali().size(); i++) {
            double x = KUALI_X + i * KUALI_JARAK;
            if (klikDiDalamKotak(klikX, klikY, x, KUALI_Y, KUALI_LEBAR, KUALI_TINGGI)) {
                game.klikKuali(i);
                return;
            }
        }

        // Cek apakah yang diklik bahan
        Ingredient[] semuaBahan = Ingredient.values();
        for (int i = 0; i < semuaBahan.length; i++) {
            double x = BAHAN_X + i * BAHAN_JARAK;
            if (klikDiDalamKotak(klikX, klikY, x, BAHAN_Y, BAHAN_LEBAR, BAHAN_TINGGI)) {
                game.klikBahan(semuaBahan[i]);
                return;
            }
        }

        // Cek apakah yang diklik tombol undo
        if (klikDiDalamKotak(klikX, klikY, UNDO_X, UNDO_Y, UNDO_LEBAR, UNDO_TINGGI)) {
            game.undo();
        }
    }

    // Mengecek apakah titik klik berada di dalam sebuah kotak
    private boolean klikDiDalamKotak(double klikX, double klikY,
                                     double kotakX, double kotakY,
                                     double lebar, double tinggi) {
        boolean dalamSisiKiriKanan = klikX >= kotakX && klikX <= kotakX + lebar;
        boolean dalamSisiAtasBawah = klikY >= kotakY && klikY <= kotakY + tinggi;
        return dalamSisiKiriKanan && dalamSisiAtasBawah;
    }

    private boolean klikDiTombol(double klikX, double klikY) {
        return klikDiDalamKotak(klikX, klikY, TOMBOL_X, TOMBOL_Y, TOMBOL_LEBAR, TOMBOL_TINGGI);
    }

    // =====================================================
    // MENGGAMBAR
    // =====================================================

    // Dipanggil GameLoop setiap frame
    public void render() {
        g.setTextAlign(TextAlignment.LEFT);
        g.setFill(Color.web("#2b2238"));
        g.fillRect(0, 0, LEBAR, TINGGI);

        ScreenState layar = game.getLayar();

        if (layar == ScreenState.MENU) {
            gambarMenu();
        } else if (layar == ScreenState.MAIN) {
            gambarLayarMain();
        } else if (layar == ScreenState.RINGKASAN) {
            gambarRingkasan();
        } else if (layar == ScreenState.GAME_OVER) {
            gambarGameOver();
        }
    }

    // ----- Layar MENU -----

    private void gambarMenu() {
        g.setTextAlign(TextAlignment.CENTER);

        g.setFill(Color.web("#e8c46a"));
        g.setFont(Font.font("System", FontWeight.BOLD, 72));
        g.fillText("POTION RUSH", LEBAR / 2, 220);

        g.setFill(Color.WHITE);
        g.setFont(Font.font(22));
        g.fillText("Brew potions to match each adventurer's order before they run out of patience.", LEBAR / 2, 300);
        g.fillText("Click an ingredient, click a cauldron, boil it, then serve the adventurer.", LEBAR / 2, 340);

        gambarTombol("START");
    }

    // ----- Layar RINGKASAN -----

    private void gambarRingkasan() {
        int hari = game.getSession().getHari();

        g.setTextAlign(TextAlignment.CENTER);

        g.setFill(Color.web("#7bd88f"));
        g.setFont(Font.font("System", FontWeight.BOLD, 56));
        g.fillText("DAY " + hari + " COMPLETE", LEBAR / 2, 170);

        g.setFill(Color.WHITE);
        g.setFont(Font.font(26));
        g.fillText("Coins today: " + game.getKoinHariIni() + " / " + game.getTargetHari(), LEBAR / 2, 260);
        g.fillText("Adventurers served: " + game.getDilayaniHariIni(), LEBAR / 2, 310);
        g.fillText("Adventurers who left: " + game.getPergiHariIni(), LEBAR / 2, 360);
        g.fillText("Total coins: " + game.getSession().getKoin(), LEBAR / 2, 410);

        gambarTombol("GO TO DAY " + (hari + 1));
    }

    // ----- Layar GAME_OVER -----

    private void gambarGameOver() {
        g.setTextAlign(TextAlignment.CENTER);

        g.setFill(Color.CRIMSON);
        g.setFont(Font.font("System", FontWeight.BOLD, 72));
        g.fillText("GAME OVER", LEBAR / 2, 200);

        g.setFill(Color.WHITE);
        g.setFont(Font.font(26));
        g.fillText(game.getAlasanGameOver(), LEBAR / 2, 280);
        g.fillText("You reached day " + game.getSession().getHari(), LEBAR / 2, 340);
        g.fillText("Total coins: " + game.getSession().getKoin(), LEBAR / 2, 390);

        gambarTombol("BACK TO MENU");
    }

    private void gambarTombol(String tulisan) {
        gambarKotak(TOMBOL_X, TOMBOL_Y, TOMBOL_LEBAR, TOMBOL_TINGGI, Color.web("#7a5fb0"));

        g.setTextAlign(TextAlignment.CENTER);
        g.setFill(Color.WHITE);
        g.setFont(Font.font("System", FontWeight.BOLD, 24));
        g.fillText(tulisan, TOMBOL_X + TOMBOL_LEBAR / 2, TOMBOL_Y + 50);
        g.setTextAlign(TextAlignment.LEFT);
    }

    // ----- Layar MAIN -----

    private void gambarLayarMain() {
        gambarHud();
        gambarPetualang();
        gambarKuali();
        gambarBahan();
        gambarTombolUndo();
        gambarPapanResep();
        gambarPesan();
    }

    private void gambarHud() {
        g.setFill(Color.WHITE);
        g.setFont(Font.font(22));
        g.fillText("Day " + game.getSession().getHari(), 30, 45);
        g.fillText("Time: " + (int) game.getSisaWaktuHari() + " s", 160, 45);
        g.fillText("Coins today: " + game.getKoinHariIni() + " / " + game.getTargetHari(), 380, 45);
        g.fillText("Total coins: " + game.getSession().getKoin(), 700, 45);
        g.fillText("Reputation: " + game.getSession().getReputasi(), 920, 45);
    }

    private void gambarPetualang() {
        ArrayList<Customer> daftarPetualang = game.getPetualang();

        for (int i = 0; i < Game.MAKS_PETUALANG; i++) {
            double x = PETUALANG_X + i * PETUALANG_JARAK;
            gambarKotak(x, PETUALANG_Y, PETUALANG_LEBAR, PETUALANG_TINGGI, Color.web("#3d3150"));

            // Slot kosong kalau belum ada petualang di posisi ini
            if (i >= daftarPetualang.size()) {
                g.setFill(Color.GRAY);
                g.setFont(Font.font(16));
                g.fillText("(empty)", x + 80, PETUALANG_Y + 90);
                continue;
            }

            Customer petualang = daftarPetualang.get(i);

            g.setFill(Color.WHITE);
            g.setFont(Font.font(20));
            g.fillText(petualang.getJenis(), x + 15, PETUALANG_Y + 35);
            g.setFont(Font.font(16));
            g.fillText("Order: " + DisplayName.ramuan(petualang.getPesanan()), x + 15, PETUALANG_Y + 75);

            // Bar kesabaran: latar gelap, lalu isi berwarna sesuai sisa kesabaran
            double lebarBar = PETUALANG_LEBAR - 30;
            double lebarIsi = lebarBar * petualang.getPersenKesabaran();

            g.setFill(Color.web("#1a1424"));
            g.fillRect(x + 15, PETUALANG_Y + 120, lebarBar, 20);
            g.setFill(warnaBarKesabaran(petualang.getTingkatKesabaran()));
            g.fillRect(x + 15, PETUALANG_Y + 120, lebarIsi, 20);
        }
    }

    private Color warnaBarKesabaran(String tingkat) {
        if (tingkat.equals("HIJAU")) {
            return Color.LIMEGREEN;
        } else if (tingkat.equals("KUNING")) {
            return Color.GOLD;
        } else {
            return Color.CRIMSON;
        }
    }

    private void gambarKuali() {
        ArrayList<Cauldron> daftarKuali = game.getKuali();

        for (int i = 0; i < daftarKuali.size(); i++) {
            Cauldron kuali = daftarKuali.get(i);
            double x = KUALI_X + i * KUALI_JARAK;

            gambarKotak(x, KUALI_Y, KUALI_LEBAR, KUALI_TINGGI, warnaKuali(kuali.getStatus()));

            g.setFill(Color.WHITE);
            g.setFont(Font.font(20));
            g.fillText("Cauldron " + (i + 1) + " - " + DisplayName.statusKuali(kuali.getStatus()), x + 15, KUALI_Y + 35);
            g.setFont(Font.font(15));
            g.fillText("Contents: " + DisplayName.daftarBahan(kuali.getIsi()), x + 15, KUALI_Y + 75);

            if (kuali.getStatus() == CauldronState.MENDIDIH) {
                int sisaDetik = (int) Math.ceil(kuali.getSisaWaktuMendidih());
                g.fillText("Time left: " + sisaDetik + " s", x + 15, KUALI_Y + 110);
            } else if (kuali.getStatus() == CauldronState.SIAP) {
                if (kuali.getHasil() == null) {
                    g.fillText("Result: FAILED", x + 15, KUALI_Y + 110);
                } else {
                    g.fillText("Result: " + DisplayName.ramuan(kuali.getHasil()), x + 15, KUALI_Y + 110);
                }
            }
        }
    }

    private Color warnaKuali(CauldronState status) {
        if (status == CauldronState.DIISI) {
            return Color.web("#4a3f6b");
        } else if (status == CauldronState.MENDIDIH) {
            return Color.web("#8a4b2a");
        } else if (status == CauldronState.SIAP) {
            return Color.web("#2f6b4a");
        } else {
            return Color.web("#3d3150");
        }
    }

    private void gambarBahan() {
        Ingredient[] semuaBahan = Ingredient.values();

        for (int i = 0; i < semuaBahan.length; i++) {
            double x = BAHAN_X + i * BAHAN_JARAK;

            // Bahan yang sedang dipegang diberi warna lebih terang
            Color warna = Color.web("#3d3150");
            if (semuaBahan[i] == game.getBahanDipegang()) {
                warna = Color.web("#7a5fb0");
            }

            gambarKotak(x, BAHAN_Y, BAHAN_LEBAR, BAHAN_TINGGI, warna);
            g.setFill(Color.WHITE);
            g.setFont(Font.font(18));
            g.fillText(DisplayName.bahan(semuaBahan[i]), x + 20, BAHAN_Y + 47);
        }
    }

    private void gambarTombolUndo() {
        gambarKotak(UNDO_X, UNDO_Y, UNDO_LEBAR, UNDO_TINGGI, Color.web("#6b2f3a"));
        g.setFill(Color.WHITE);
        g.setFont(Font.font(18));
        g.fillText("UNDO", UNDO_X + 45, UNDO_Y + 47);
    }

    private void gambarPapanResep() {
        double x = 1000;
        double y = 90;
        gambarKotak(x, y, 250, 410, Color.web("#3a2c22"));

        g.setFill(Color.WHEAT);
        g.setFont(Font.font(20));
        g.fillText("RECIPES", x + 80, y + 35);

        // Setiap resep ditulis dua baris: nama ramuan, lalu urutan bahannya
        g.setFont(Font.font(14));
        double barisY = y + 80;
        for (Recipe resep : Recipe.semuaResep()) {
            g.fillText(DisplayName.ramuan(resep.getHasil()) + ":", x + 15, barisY);
            g.fillText(DisplayName.daftarBahan(resep.getUrutanBahan()), x + 15, barisY + 20);
            barisY = barisY + 70;
        }
    }

    private void gambarPesan() {
        String barangDipegang = "Holding nothing";
        if (game.getBahanDipegang() != null) {
            barangDipegang = "Holding: " + DisplayName.bahan(game.getBahanDipegang());
        } else if (game.getRamuanDipegang() != null) {
            barangDipegang = "Holding: " + DisplayName.ramuan(game.getRamuanDipegang());
        }

        g.setFill(Color.WHITE);
        g.setFont(Font.font(18));
        g.fillText(barangDipegang, 30, 685);
        g.fillText(game.getPesan(), 400, 685);
    }

    private void gambarKotak(double x, double y, double lebar, double tinggi, Color warna) {
        g.setFill(warna);
        g.fillRoundRect(x, y, lebar, tinggi, 16, 16);
    }
}