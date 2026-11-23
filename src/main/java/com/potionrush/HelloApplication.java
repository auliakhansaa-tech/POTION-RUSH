package com.potionrush;

import com.potionrush.core.Game;
import com.potionrush.core.GameLoop;
import com.potionrush.ui.GameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        Game game = new Game();
        GameView view = new GameView(game);

        Scene scene = new Scene(view, GameView.LEBAR, GameView.TINGGI);
        stage.setTitle("Potion Rush");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        new GameLoop(game, view::render).start();
    }
}