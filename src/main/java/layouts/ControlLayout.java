package main.java.layouts;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import jssc.SerialPortException;
import main.java.externalcode.IntField;
import main.java.utilities.ParamValueChange;

/**
 * This class creates the layout for each individual parameter control
 * Currently creates an IntField and a Label
 * TODO: Add knobs/sliders
 */
public class ControlLayout {
    // private final Integer paramValue = 0;
    private final IntField paramField = new IntField(0, 255,0);
    Label paramName = new Label("DEFAULT");
    BorderPane layoutBorder;
    public ControlLayout(String p){
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(255);

        String[] paramSplit = p.split(":");

        paramName.getStyleClass().add("param-label");
        paramName.setText(paramSplit[0]);
        paramName.setAlignment(Pos.CENTER);
        paramName.setPrefWidth(100);

        paramField.setMaxWidth(45);
        paramField.setAlignment(Pos.CENTER);
        paramField.setId(paramSplit[1]);

        slider.getStyleClass().add("slider-new");
        slider.setMaxWidth(80);

        paramField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ParamValueChange.onFieldChange(paramField.getValue(),paramField,slider);
            } catch (SerialPortException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        slider.valueProperty().addListener((observableValue, number, t1) -> ParamValueChange.onSliderChange((int) slider.getValue(),paramField,slider));

        layoutBorder = new BorderPane();
        //layoutBorder.getStylesheets().add(style);
        layoutBorder.setTop(paramName);
        layoutBorder.setCenter(slider);
        layoutBorder.setBottom(paramField);
        BorderPane.setAlignment(paramField, Pos.CENTER);
    }

    public BorderPane getLayout(){
        return layoutBorder;
    }

    public IntField getParamField(){
        return paramField;
    }
}
