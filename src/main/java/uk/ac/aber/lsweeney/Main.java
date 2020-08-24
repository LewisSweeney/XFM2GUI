package uk.ac.aber.lsweeney;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
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
 */
public class Main extends Application {

    SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

    AlertHandler alertHandler = new AlertHandler();

    MenuEventHandler menuEventHandler = MenuEventHandler.getSingleInstance();
    OptionsHandler optionsHandler = OptionsHandler.getSingleInstance();


    @Override
    public void start(Stage primaryStage) throws IOException, SerialPortException {

        Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));

        MenuInitialiser menuInitialiser = new MenuInitialiser();
        Scene scene = menuInitialiser.initializeScene();
        ParamValueChangeHandler.setSerialHandler(serialHandlerBridge);

        optionsHandler.setLiveChanges(false);
        menuEventHandler.setAllIntFieldValues(serialHandlerBridge.getAllValues());

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("XFM2GUI");

        primaryStage.getIcons().add(logo);
        primaryStage.show();


        byte[] data = serialHandlerBridge.getAllValues();

        if (!serialHandlerBridge.isThereASerialPort()) {
            if(!System.getProperty("os.name").toLowerCase().contains("windows") && !System.getProperty("os.name").toLowerCase().contains("mac")){
                alertHandler.sendAlert(ALERT_TYPE.LINUX);
            }
            alertHandler.sendAlert(ALERT_TYPE.NO_DEVICE);
        } else if (data.length != 512) {
            alertHandler.sendAlert(ALERT_TYPE.NOT_XFM);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


}
