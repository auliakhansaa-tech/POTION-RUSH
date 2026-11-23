package com.potionrush.core;

import com.potionrush.ui.GameView;
import javafx.animation.AnimationTimer;

// Penggerak game. JavaFX memanggil handle() sekitar 60 kali per detik.
public class GameLoop extends AnimationTimer {

    private Game game;
    private GameView view;
    private long waktuSebelumnya = 0;

    public GameLoop(Game game, GameView view) {
        this.game = game;
        this.view = view;
    }

    // now = waktu saat ini dalam nanodetik
    @Override
    public void handle(long now) {
        if (waktuSebelumnya == 0) {
            waktuSebelumnya = now;
            return;
        }

        // Ubah selisih waktu dari nanodetik ke detik
        double dt = (now - waktuSebelumnya) / 1000000000.0;
        waktuSebelumnya = now;

        game.update(dt);
        view.render();
    }
}