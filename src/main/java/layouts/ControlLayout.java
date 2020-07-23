package main.java.layouts;

import eu.hansolo.medusa.Gauge;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.java.utilities.IntField;
import main.java.utilities.ParamValueChange;

/**
 * This class creates the layout for each individual parameter control
 * Currently creates an IntField and a Label
 * TODO: Add knobs/sliders
 */
public class ControlLayout {
    private final Integer paramValue = 0;
    private final IntField paramField = new IntField(0, 255,0);
    Label paramName = new Label("DEFAULT");
    BorderPane layoutBorder;
    public ControlLayout(String p){
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(255);

        paramName.getStyleClass().add("param-label");
        paramName.setText(p);
        paramName.setAlignment(Pos.CENTER);
        paramName.setPrefWidth(100);

        paramField.setMaxWidth(45);
        paramField.setAlignment(Pos.CENTER);

        slider.getStyleClass().add("slider-new");
        slider.setMaxWidth(80);

        paramField.textProperty().addListener((observable, oldValue, newValue) -> ParamValueChange.onFieldChange(paramField.getValue(),paramField,slider));

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
}
