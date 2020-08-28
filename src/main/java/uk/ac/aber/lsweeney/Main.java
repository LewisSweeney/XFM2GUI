package uk.ac.aber.lsweeney;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jssc.SerialPortException;
import uk.ac.aber.lsweeney.enums.ALERT_TYPE;
import uk.ac.aber.lsweeney.functionhandlers.AlertHandler;
import uk.ac.aber.lsweeney.functionhandlers.MenuEventHandler;
import uk.ac.aber.lsweeney.functionhandlers.OptionsHandler;
import uk.ac.aber.lsweeney.functionhandlers.ParamValueChangeHandler;
import uk.ac.aber.lsweeney.initializers.MenuInitialiser;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

import java.io.IOException;

/**
 * Starts the program, constructing the GUI and readying the system for XFM communication
 * The system also displays a splash screen that stays on screen until the initialisation is complete
 *
 * Splash screen code adapted from a github post by jewelsea:
 * https://gist.github.com/jewelsea/2305098
 */
public class Main extends Application {

    private BorderPane splashLayout;
    private ProgressBar loadProgress;
    private static final int SPLASH_WIDTH = 256;
    private static final int SPLASH_HEIGHT = 256;

    SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

    AlertHandler alertHandler = new AlertHandler();

    MenuEventHandler menuEventHandler = MenuEventHandler.getSingleInstance();
    OptionsHandler optionsHandler = OptionsHandler.getSingleInstance();

    Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
    Scene scene;
    byte[] data;

    String style = this.getClass().getResource("/stylesheets/splash.css").toExternalForm();


    @Override
    public void start(final Stage initStage) {
        final Task<ObservableList<String>> splashTask = new Task<>() {
            final MenuInitialiser menuInitialiser = new MenuInitialiser();

            @Override
            protected ObservableList<String> call() throws IOException, SerialPortException {
                ObservableList<String> requiredList = FXCollections.<String>observableArrayList();
                scene = menuInitialiser.initializeScene();
                ParamValueChangeHandler.setSerialHandler(serialHandlerBridge);
                optionsHandler.setLiveChanges(false);
                data = menuInitialiser.getReadData();
                menuEventHandler.setAllIntFieldValues(data);
                return requiredList;
            }
        };

        showSplash(
                initStage,
                splashTask,
                () -> {
                    try {
                        showPrimaryStage(splashTask.valueProperty());
                    } catch (IOException | SerialPortException e) {
                        e.printStackTrace();
                    }
                }
        );
        new Thread(splashTask).start();
    }

    private void showPrimaryStage(ReadOnlyObjectProperty<ObservableList<String>> friends) throws IOException, SerialPortException {

        Stage primaryStage = new Stage(StageStyle.UTILITY);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("XFM2GUI");
        primaryStage.setMaxWidth(950);
        primaryStage.setMaxHeight(700);

        primaryStage.getIcons().add(logo);
        primaryStage.show();

        if (!serialHandlerBridge.isThereASerialPort()) {
            if (!System.getProperty("os.name").toLowerCase().contains("windows") && !System.getProperty("os.name").toLowerCase().contains("mac")) {
                alertHandler.sendAlert(ALERT_TYPE.LINUX);
            }
            alertHandler.sendAlert(ALERT_TYPE.NO_DEVICE);
        } else if (data == null || data.length != 512) {
            alertHandler.sendAlert(ALERT_TYPE.NOT_XFM);
        }
    }

    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            }
        });

        Scene splashScene = new Scene(splashLayout);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        splashScene.getStylesheets().add(style);
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    @Override
    public void init() {
        loadProgress = new ProgressBar();

        ImageView splash = new ImageView(logo);
        splash.setFitHeight(256);
        splash.setFitWidth(256);

        splashLayout = new BorderPane();
        splashLayout.getStyleClass().add("splash-layout");
        loadProgress.getStyleClass().add("load-bar");
        splashLayout.setCenter(splash);
        splashLayout.setBottom(loadProgress);
        BorderPane.setAlignment(loadProgress,Pos.CENTER);
        splashLayout.setEffect(new DropShadow());
    }

    public interface InitCompletionHandler {
        void complete();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
