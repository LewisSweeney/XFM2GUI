package uk.ac.aber.lsweeney;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import uk.ac.aber.lsweeney.enums.ALERT_TYPE;
import uk.ac.aber.lsweeney.functionhandlers.*;
import uk.ac.aber.lsweeney.initializers.MenuInitialiser;
import uk.ac.aber.lsweeney.serial.other.SerialHandlerOther;

import java.io.IOException;

/**
 * Starts the program, constructing the GUI and readying the system for XFM communication
 */
public class Main extends Application {

    String[] serialPortNameList = SerialPortList.getPortNames();
    SerialPort serialPort;
    SerialHandlerOther serialHandlerOther = new SerialHandlerOther(serialPort);

    AlertHandler alertHandler = new AlertHandler();

    MenuEventHandler menuEventHandler = MenuEventHandler.getSingleInstance();
    OptionsHandler optionsHandler = OptionsHandler.getSingleInstance();


    @Override
    public void start(Stage primaryStage) throws IOException, SerialPortException {

        MenuInitialiser menuInitialiser = new MenuInitialiser(serialHandlerOther, serialPort, serialPortNameList);
        Scene scene = menuInitialiser.initializeScene();
        ParamValueChangeHandler.setSerialHandlerOther(serialHandlerOther);


        optionsHandler.setLiveChanges(false);
        menuEventHandler.setAllIntFieldValues(serialHandlerOther.getAllValues());
        optionsHandler.setLiveChanges(true);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("XFM2GUI");
        primaryStage.show();

        if (serialHandlerOther.getSerialPort() == null) {
            alertHandler.SendAlert(ALERT_TYPE.NO_DEVICE);
        } else if (serialHandlerOther.getAllValues().length != 512 || serialHandlerOther.getAllValues().length != 287) {
            alertHandler.SendAlert(ALERT_TYPE.NOT_XFM);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


}
