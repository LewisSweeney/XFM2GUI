package uk.ac.aber.lsweeney.controls;

import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import uk.ac.aber.lsweeney.functionhandlers.ParamValueChangeHandler;

public class SliderControl extends ParameterControl {

    Slider slider;
    BorderPane borderPane;

    public SliderControl(String name, String paramID, int maxVal) {
        super(name, paramID, maxVal);

        slider = new Slider();
        slider.setMax(maxVal);

        slider.valueProperty().addListener((observableValue, number, t1) -> ParamValueChangeHandler.onSliderChange(this));
        constructLayout();
    }

    private void constructLayout() {
        slider.getStyleClass().add("slider-new");
        slider.setMaxWidth(80);
        paramField.setMaxWidth(45);
        paramField.setAlignment(Pos.CENTER);
        paramName.getStyleClass().add("param-label");


        borderPane = new BorderPane();
        borderPane.setTop(paramName);
        borderPane.setCenter(slider);
        borderPane.setBottom(paramField);
        BorderPane.setAlignment(paramField, Pos.CENTER);
        borderPane.getStyleClass().add("control");
    }

    @Override
    public Slider getSlider() {
        return slider;
    }

    @Override
    public BorderPane getLayout() {

        return borderPane;
    }
}
