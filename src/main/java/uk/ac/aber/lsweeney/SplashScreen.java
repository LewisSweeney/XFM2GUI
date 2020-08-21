package uk.ac.aber.lsweeney;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashScreen extends Preloader {

    Stage splashStage;

    @Override
    public void init() throws Exception {
        Platform.runLater(() -> {
            splashStage = new Stage();
            splashStage.initStyle(StageStyle.TRANSPARENT);
            splashStage.setScene(createScene());
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        splashStage.show();
    }

    private Scene createScene() {
        ImageView splash = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
        splash.setFitHeight(200);
        splash.setFitWidth(200);
        VBox stageBox = new VBox(splash);
        Scene scene = new Scene(stageBox, 200, 200);


        return scene;

    }

    @Override
    public void handleApplicationNotification(PreloaderNotification arg0) {
        if (arg0 instanceof ProgressNotification) {
            splashStage.close();
        }
    }


}
