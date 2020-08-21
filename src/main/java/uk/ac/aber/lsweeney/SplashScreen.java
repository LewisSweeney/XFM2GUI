package uk.ac.aber.lsweeney;

import javafx.application.Preloader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SplashScreen extends Preloader {

    @Override
    public void init() throws Exception {
        ImageView splash = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
