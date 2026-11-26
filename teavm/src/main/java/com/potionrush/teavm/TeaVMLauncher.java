package com.potionrush.teavm;

import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.potionrush.PotionRushGame;

/**
 * Launches the TeaVM/HTML application.
 */
public class TeaVMLauncher {
    public static void main(String[] args) {
        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        config.width = 0;    // 0 and 0 = canvas fills the whole browser window
        config.height = 0;
        new TeaApplication(new PotionRushGame(), config);
    }
}
