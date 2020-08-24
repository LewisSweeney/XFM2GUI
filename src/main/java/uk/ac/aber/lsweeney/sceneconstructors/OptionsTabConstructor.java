package uk.ac.aber.lsweeney.sceneconstructors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jssc.SerialPortException;
import uk.ac.aber.lsweeney.enums.FILE_TYPE;
import uk.ac.aber.lsweeney.functionhandlers.FileLoader;
import uk.ac.aber.lsweeney.functionhandlers.OptionsHandler;

import java.io.IOException;

public class OptionsTabConstructor {
    private final BorderPane border;
    Stage stage;
    Scene scene;
    VBox testOption = new VBox();
    CheckBox darkMode;

    String style = this.getClass().getResource("/stylesheets/style.css").toExternalForm();
    String dark =  this.getClass().getResource("/stylesheets/darkmode.css").toExternalForm();

    FileLoader fileLoader = new FileLoader();

    OptionsHandler optionsHandler = OptionsHandler.getSingleInstance();

    public OptionsTabConstructor(BorderPane border){

        this.border = border;
        darkMode = new CheckBox("Dark Mode");
        darkMode.setOnAction(actionEvent -> onDarkModeToggle());
        testOption.getChildren().add(writeOptions());
    }

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

    private void onDarkModeToggle(){
        scene.getStylesheets().removeAll();
        if(darkMode.isSelected()){
            scene.getStylesheets().addAll(style,dark);
        } else{
            scene.getStylesheets().add(style);
        }
        stage.setScene(scene);
    }
}
