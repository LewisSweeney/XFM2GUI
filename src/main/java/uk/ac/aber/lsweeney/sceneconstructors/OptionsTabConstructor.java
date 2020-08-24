package uk.ac.aber.lsweeney.sceneconstructors;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jssc.SerialPortException;
import uk.ac.aber.lsweeney.enums.FILE_TYPE;
import uk.ac.aber.lsweeney.functionhandlers.OptionsHandler;

import java.io.IOException;

/**
 * UNUSED
 * Class to construct the Options tab
 */
@SuppressWarnings("unused")
public class OptionsTabConstructor {
    private final BorderPane border;
    Stage stage;
    Scene scene;
    VBox testOption = new VBox();

    String style = this.getClass().getResource("/stylesheets/style.css").toExternalForm();

    OptionsHandler optionsHandler = OptionsHandler.getSingleInstance();

    /**
     * UNUSED
     * @param border BorderPane to be used for creating an options screen
     */
    public OptionsTabConstructor(BorderPane border){

        this.border = border;
        testOption.getChildren().add(writeOptions());
    }

    /**
     * Creates the option buttons for the file writing actions
     * @return VBox containing these buttons
     */
    private VBox writeOptions(){

        Button writeTuning = new Button("Write Tuning File");
        Button writeBank = new Button("Write Bank File");

        writeBank.setOnAction(actionEvent -> {
            try {
                border.setDisable(true);
                optionsHandler.writeFileToDevice(FILE_TYPE.BANK);
                border.setDisable(false);
            } catch (IOException | SerialPortException e) {
                e.printStackTrace();
            }
        });

        writeTuning.setOnAction(actionEvent -> {
            try {
                border.setDisable(true);
                optionsHandler.writeFileToDevice(FILE_TYPE.TUNING);
                border.setDisable(false);
            } catch (IOException | SerialPortException e) {
                e.printStackTrace();
            }
        });


        return new VBox(writeBank,writeTuning);
    }

    public VBox getLayout(){
        return testOption;
    }

}
