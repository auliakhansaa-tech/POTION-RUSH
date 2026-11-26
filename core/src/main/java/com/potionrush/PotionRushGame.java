package com.potionrush;

import com.badlogic.gdx.Game;
import com.potionrush.ui.GameScreen;

/** Entry point shared by the desktop and browser launchers. */
public class PotionRushGame extends Game {

    private GameScreen gameScreen;

    @Override
    public void create() {
        gameScreen = new GameScreen();
        setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (gameScreen != null) {
            gameScreen.dispose();
        }
    }
}
