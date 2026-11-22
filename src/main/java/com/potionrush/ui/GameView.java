package com.potionrush.ui;

import com.potionrush.brewing.Cauldron;
import com.potionrush.brewing.CauldronState;
import com.potionrush.brewing.Ingredient;
import com.potionrush.brewing.PotionType;
import com.potionrush.core.Game;
import com.potionrush.customer.Customer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

// Tampilan permainan: menggambar isi Game ke layar dan menerima klik mouse
public class GameView extends Pane {
    public static final int LEBAR = 1280;
    public static final int TINGGI = 720;

    // Posisi area-area yang bisa diklik
    private static final double PETUALANG_X = 60, PETUALANG_Y = 90, PETUALANG_W = 220, PETUALANG_H = 170, PETUALANG_JARAK = 250;
    private static final double KUALI_X = 120, KUALI_Y = 330, KUALI_W = 240, KUALI_H = 170, KUALI_JARAK = 300;
    private static final double BAHAN_X = 60, BAHAN_Y = 560, BAHAN_W = 140, BAHAN_H = 80, BAHAN_JARAK = 160;
    private static final double UNDO_X = 880, UNDO_Y = 560, UNDO_W = 140, UNDO_H = 80;

    private final Game game;
    private final Canvas canvas = new Canvas(LEBAR, TINGGI);
    private final GraphicsContext g = canvas.getGraphicsContext2D();

    public GameView(Game game) {
        this.game = game;
        getChildren().add(canvas);
        canvas.setOnMouseClicked(e -> tanganiKlik(e.getX(), e.getY()));
    }

    // ===================== KLIK MOUSE =====================

    private void tanganiKlik(double x, double y) {
        // Petualang
        for (int i = 0; i < Game.MAKS_PETUALANG; i++) {
            if (di(x, y, PETUALANG_X + i * PETUALANG_JARAK, PETUALANG_Y, PETUALANG_W, PETUALANG_H)) {
                game.klikPetualang(i);
                return;
            }
        }
        // Kuali
        for (int i = 0; i < game.getKuali().size(); i++) {
            if (di(x, y, KUALI_X + i * KUALI_JARAK, KUALI_Y, KUALI_W, KUALI_H)) {
                game.klikKuali(i);
                return;
            }
        }
        // Bahan
        Ingredient[] semuaBahan = Ingredient.values();
        for (int i = 0; i < semuaBahan.length; i++) {
            if (di(x, y, BAHAN_X + i * BAHAN_JARAK, BAHAN_Y, BAHAN_W, BAHAN_H)) {
                game.klikBahan(semuaBahan[i]);
                return;
            }
        }
        // Tombol Undo
        if (di(x, y, UNDO_X, UNDO_Y, UNDO_W, UNDO_H)) {
            game.undo();
        }
    }

    // true kalau titik (x, y) ada di dalam kotak
    private boolean di(double x, double y, double kx, double ky, double kw, double kh) {
        return x >= kx && x <= kx + kw && y >= ky && y <= ky + kh;
    }

    // ===================== MENGGAMBAR =====================

    // Dipanggil GameLoop setiap frame
    public void render() {
        g.setFill(Color.web("#2b2238"));
        g.fillRect(0, 0, LEBAR, TINGGI);

        gambarHud();
        gambarPetualang();
        gambarKuali();
        gambarBahan();
        gambarPapanResep();
        gambarPesan();
    }

    private void gambarHud() {
        g.setFill(Color.WHITE);
        g.setFont(Font.font(22));
        g.fillText("Hari " + game.getSession().getHari(), 30, 45);
        g.fillText("Waktu: " + (int) game.getSisaWaktuHari() + " dtk", 200, 45);
        g.fillText("Koin: " + game.getSession().getKoin() + " / " + Game.TARGET_KOIN, 450, 45);
        g.fillText("Reputasi: " + game.getSession().getReputasi(), 700, 45);
    }

    private void gambarPetualang() {
        List<Customer> daftar = game.getPetualang();
        for (int i = 0; i < Game.MAKS_PETUALANG; i++) {
            double x = PETUALANG_X + i * PETUALANG_JARAK;
            kotak(x, PETUALANG_Y, PETUALANG_W, PETUALANG_H, Color.web("#3d3150"));
            if (i >= daftar.size()) {
                g.setFill(Color.GRAY);
                g.setFont(Font.font(16));
                g.fillText("(kosong)", x + 75, PETUALANG_Y + 90);
                continue;
            }
            Customer c = daftar.get(i);
            g.setFill(Color.WHITE);
            g.setFont(Font.font(20));
            g.fillText(c.getJenis(), x + 15, PETUALANG_Y + 35);
            g.setFont(Font.font(16));
            g.fillText("Pesan: " + c.getPesanan(), x + 15, PETUALANG_Y + 75);

            // Bar kesabaran
            double lebarBar = PETUALANG_W - 30;
            g.setFill(Color.web("#1a1424"));
            g.fillRect(x + 15, PETUALANG_Y + 120, lebarBar, 20);
            g.setFill(warnaBar(c.getTingkatKesabaran()));
            g.fillRect(x + 15, PETUALANG_Y + 120, lebarBar * c.getPersenKesabaran(), 20);
        }
    }

    private Color warnaBar(String tingkat) {
        if (tingkat.equals("HIJAU")) {
            return Color.LIMEGREEN;
        } else if (tingkat.equals("KUNING")) {
            return Color.GOLD;
        }
        return Color.CRIMSON;
    }

    private void gambarKuali() {
        List<Cauldron> daftar = game.getKuali();
        for (int i = 0; i < daftar.size(); i++) {
            Cauldron k = daftar.get(i);
            double x = KUALI_X + i * KUALI_JARAK;
            kotak(x, KUALI_Y, KUALI_W, KUALI_H, warnaKuali(k.getStatus()));

            g.setFill(Color.WHITE);
            g.setFont(Font.font(20));
            g.fillText("Kuali " + (i + 1) + " - " + k.getStatus(), x + 15, KUALI_Y + 35);
            g.setFont(Font.font(15));
            g.fillText("Isi: " + k.getIsi(), x + 15, KUALI_Y + 75);

            if (k.getStatus() == CauldronState.MENDIDIH) {
                g.fillText("Sisa: " + String.format("%.1f", k.getSisaWaktuMendidih()) + " dtk", x + 15, KUALI_Y + 110);
            } else if (k.getStatus() == CauldronState.SIAP) {
                PotionType hasil = k.getHasil();
                String teks = (hasil == null) ? "GAGAL" : hasil.toString();
                g.fillText("Hasil: " + teks, x + 15, KUALI_Y + 110);
            }
        }
    }

    private Color warnaKuali(CauldronState status) {
        switch (status) {
            case DIISI:
                return Color.web("#4a3f6b");
            case MENDIDIH:
                return Color.web("#8a4b2a");
            case SIAP:
                return Color.web("#2f6b4a");
            default:
                return Color.web("#3d3150");
        }
    }

    private void gambarBahan() {
        Ingredient[] semuaBahan = Ingredient.values();
        for (int i = 0; i < semuaBahan.length; i++) {
            double x = BAHAN_X + i * BAHAN_JARAK;
            Color warna = (semuaBahan[i] == game.getBahanDipegang()) ? Color.web("#7a5fb0") : Color.web("#3d3150");
            kotak(x, BAHAN_Y, BAHAN_W, BAHAN_H, warna);
            g.setFill(Color.WHITE);
            g.setFont(Font.font(18));
            g.fillText(semuaBahan[i].toString(), x + 20, BAHAN_Y + 47);
        }
        kotak(UNDO_X, UNDO_Y, UNDO_W, UNDO_H, Color.web("#6b2f3a"));
        g.setFill(Color.WHITE);
        g.fillText("UNDO", UNDO_X + 45, UNDO_Y + 47);
    }

    private void gambarPapanResep() {
        double x = 1000, y = 90;
        kotak(x, y, 250, 410, Color.web("#3a2c22"));
        g.setFill(Color.WHEAT);
        g.setFont(Font.font(20));
        g.fillText("PAPAN RESEP", x + 60, y + 35);
        g.setFont(Font.font(14));
        g.fillText("PENYEMBUH:", x + 15, y + 80);
        g.fillText("DAUN > KRISTAL", x + 15, y + 100);
        g.fillText("KEKUATAN:", x + 15, y + 150);
        g.fillText("JAMUR > TULANG", x + 15, y + 170);
        g.fillText("RACUN:", x + 15, y + 220);
        g.fillText("JAMUR > DAUN > TULANG", x + 15, y + 240);
        g.fillText("TAK_TERLIHAT:", x + 15, y + 290);
        g.fillText("BUNGA > KRISTAL > DAUN", x + 15, y + 310);
    }

    private void gambarPesan() {
        g.setFill(Color.WHITE);
        g.setFont(Font.font(18));
        String dipegang = "Tangan kosong";
        if (game.getBahanDipegang() != null) {
            dipegang = "Memegang bahan: " + game.getBahanDipegang();
        } else if (game.getRamuanDipegang() != null) {
            dipegang = "Memegang ramuan: " + game.getRamuanDipegang();
        }
        g.fillText(dipegang, 30, 685);
        g.fillText(game.getPesan(), 400, 685);
    }

    private void kotak(double x, double y, double w, double h, Color warna) {
        g.setFill(warna);
        g.fillRoundRect(x, y, w, h, 16, 16);
    }
}