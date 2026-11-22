package com.potionrush.core;

import javafx.animation.AnimationTimer;

// Penggerak permainan: dijalankan JavaFX sekitar 60 kali per detik
public class GameLoop extends AnimationTimer {
    private final Game game;
    private final Runnable gambarUlang;
    private long waktuSebelumnya = 0;

    // gambarUlang = method yang menggambar layar (dari GameView)
    public GameLoop(Game game, Runnable gambarUlang) {
        this.game = game;
        this.gambarUlang = gambarUlang;
    }

    // "now" adalah waktu saat ini dalam nanodetik
    @Override
    public void handle(long now) {
        if (waktuSebelumnya == 0) {
            waktuSebelumnya = now;
            return;
        }
        double dt = (now - waktuSebelumnya) / 1_000_000_000.0; // nanodetik ke detik
        waktuSebelumnya = now;

        game.update(dt);
        gambarUlang.run();
    }
}