package main.java.layouts;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jssc.SerialPortException;
import main.java.externalcode.IntField;
import main.java.utilities.ParamValueChange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class creates the layout for each individual parameter control
 * Currently creates an IntField and a Label
 * TODO: Add knobs/sliders
 */
public class ControlLayout {
    // private final Integer paramValue = 0;
    private IntField paramField = new IntField(0, 255,0);
    Label paramName = new Label("DEFAULT");
    BorderPane layoutBorder;
    boolean bitwise = false;
    CheckBox[] bitwiseCheckboxes;
    Label[] labels;
    Slider slider = new Slider();
    public ControlLayout(String p){
        String[] paramSplit = p.split(":");
        paramName.getStyleClass().add("param-label");
        paramName.setText(paramSplit[0].toUpperCase());
        paramName.setAlignment(Pos.CENTER);
        paramName.setPrefWidth(100);
        paramField.setId(paramSplit[1]);


        paramField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ParamValueChange.onFieldChange(paramField.getValue(), paramField, slider, bitwiseCheckboxes);
            } catch (SerialPortException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });


        if(paramSplit.length > 2 && paramSplit[2].equals("BIT")){
            VBox[] checkAndLabel = new VBox[Integer.parseInt(paramSplit[3])];
            bitwise = true;
            labels = new Label[Integer.parseInt(paramSplit[3])];
            bitwiseCheckboxes = new CheckBox[Integer.parseInt(paramSplit[3])];

            String[] labelSplit = paramSplit[4].split("/");

            for(int i = 0;i<labels.length;i++){
                labels[i] = new Label(labelSplit[i]);
                bitwiseCheckboxes[i] = new CheckBox();
            }

            int bitNums = Integer.parseInt(paramSplit[3]);
            paramField.setBitwise(true);
            for(int i = 0;i<bitNums;i++){
                checkAndLabel[i] = new VBox(labels[i],bitwiseCheckboxes[i]);
                checkAndLabel[i].getStyleClass().add("check-layout");
                bitwiseCheckboxes[i].setOnAction(e ->{
                    onCheckChange(Integer.parseInt(paramSplit[3]));
                });
            }

            HBox setOfBoxes = new HBox(checkAndLabel);
            setOfBoxes.getStyleClass().add("check-groups");

            layoutBorder = new BorderPane();
            //layoutBorder.getStylesheets().add(style);
            layoutBorder.setTop(paramName);
            layoutBorder.setCenter(setOfBoxes);
            BorderPane.setAlignment(paramName, Pos.CENTER);
            BorderPane.setAlignment(setOfBoxes,Pos.CENTER);

        } else {
            slider.setMin(0);
            slider.setMax(255);


            if (paramSplit.length > 2 && !paramSplit[2].equals("BIT")) {
                slider.setMax(Double.parseDouble(paramSplit[2]));
                paramField = new IntField(0, Integer.parseInt(paramSplit[2]), 0);
            }
            paramField.setMaxWidth(45);
            paramField.setAlignment(Pos.CENTER);
            paramField.setId(paramSplit[1]);

            slider.getStyleClass().add("slider-new");
            slider.setMaxWidth(80);

            paramField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    ParamValueChange.onFieldChange(paramField.getValue(), paramField, slider, bitwiseCheckboxes);
                } catch (SerialPortException | InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            });

            slider.valueProperty().addListener((observableValue, number, t1) -> ParamValueChange.onSliderChange((int) slider.getValue(), paramField, slider));

            layoutBorder = new BorderPane();
            //layoutBorder.getStylesheets().add(style);
            layoutBorder.setTop(paramName);
            layoutBorder.setCenter(slider);
            layoutBorder.setBottom(paramField);
            BorderPane.setAlignment(paramField, Pos.CENTER);
        }
    }

    public BorderPane getLayout(){
        return layoutBorder;
    }

    public IntField getParamField(){
        return paramField;
    }

    private void onCheckChange(int bitNum){
        byte paramValue = 0;
        for(byte i = 0;i<bitNum;i++){
            if(bitwiseCheckboxes[i].isSelected()){
                byte add = 1;
                byte iter = (byte) (bitNum - i);
                for(byte j = 1;j < iter;j++) {
                    add = (byte) (add * 2);
                }
                paramValue = (byte) (paramValue + add);
            }
        }
        System.out.println(paramValue);
        paramField.setValue(paramValue & 0xFF);


    }
}
