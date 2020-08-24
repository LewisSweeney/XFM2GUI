package uk.ac.aber.lsweeney.controls;

import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import jssc.SerialPortException;
import uk.ac.aber.lsweeney.externalcode.IntField;
import uk.ac.aber.lsweeney.functionhandlers.ParamValueChangeHandler;

import java.io.IOException;

public abstract class ParameterControl {
    Label paramName;
    IntField paramField;
    BorderPane borderPane;


    public ParameterControl(String name, String paramID, int maxVal){
        this.paramName = new Label(name);

        this.paramField = new IntField(0,maxVal,0);
        this.paramField.setId(paramID);
        this.paramField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ParamValueChangeHandler.onFieldChange(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        paramName.getStyleClass().add("param-label");
    }

    public IntField getParamField(){
        return paramField;
    }

    public BorderPane getLayout(){
        return borderPane;
    }

    public Slider getSlider(){
        return null;
    }

    public CheckBox[] getBitwiseCheckboxes(){
        return null;
    }

    public ComboBox<String> getWaves(){
        return null;
    }

    public ImageView getWaveImage(){
        return null;
    }

    public void setWaveImage(int val){
    }

    public RadioButton[] getRadioButtons(){
        return null;
    }
}
