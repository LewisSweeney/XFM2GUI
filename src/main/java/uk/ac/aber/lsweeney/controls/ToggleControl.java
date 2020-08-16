package uk.ac.aber.lsweeney.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ToggleControl extends ParameterControl {

    RadioButton[] radioButtons;
    BorderPane borderPane;

    public ToggleControl(String name, String paramID, int maxVal) {
        super(name, paramID, maxVal);
        radioButtons = new RadioButton[2];
        constructLayout();
    }

    private void constructLayout(){

        ToggleGroup toggleGroup = new ToggleGroup();

        Label mode0Label = new Label("0");
        radioButtons[0] = new RadioButton();
        radioButtons[0].setToggleGroup(toggleGroup);
        VBox mode0Box = new VBox(mode0Label,radioButtons[0]);
        mode0Box.getStyleClass().add("check-layout");

        Label mode1Label = new Label("1");
        radioButtons[1] = new RadioButton();
        radioButtons[1].setToggleGroup(toggleGroup);
        VBox mode1Box = new VBox(mode1Label,radioButtons[1]);
        mode1Box.getStyleClass().add("check-layout");

        HBox radioButtonBox = new HBox(mode0Box,mode1Box);
        radioButtonBox.getStyleClass().add("check-groups");
        radioButtonBox.setStyle("-fx-alignment: center");

        borderPane = new BorderPane();
        borderPane.setTop(paramName);
        borderPane.setCenter(radioButtonBox);
        BorderPane.setAlignment(paramName, Pos.CENTER);
        BorderPane.setAlignment(radioButtonBox, Pos.CENTER);
        borderPane.getStyleClass().add("control");

        radioButtons[0].fire();

        EventHandler<ActionEvent> mode0EventHandler = actionEvent -> onToggle(false);
        radioButtons[0].setOnAction(mode0EventHandler);

        EventHandler<ActionEvent> mode1EventHandler = actionEvent -> onToggle(true);
        radioButtons[1].setOnAction(mode1EventHandler);
    }

    @Override
    public BorderPane getLayout(){
        return borderPane;
    }

    @Override
    public RadioButton[] getRadioButtons(){
        return radioButtons;
    }

    /**
     * Changes the value in the hidden paramfield depending on the radio button that's selected
     * @param selected The true or false state passed from the method
     */
    private void onToggle(boolean selected) {
        if(selected){
            paramField.setValue(1);
        } else{
            paramField.setValue(0);
        }
    }

}
