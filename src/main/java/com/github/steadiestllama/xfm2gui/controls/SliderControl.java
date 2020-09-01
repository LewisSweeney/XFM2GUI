package com.github.steadiestllama.xfm2gui.controls;

/*

This file is part of XFM2GUI

Copyright 2020 Lewis Sweeney

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

import com.github.steadiestllama.xfm2gui.functionhandlers.ParamValueChangeHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;

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
        //slider.setSnapToTicks(true);
        paramField.setMaxWidth(45);
        paramField.setAlignment(Pos.CENTER);
        paramName.getStyleClass().add("param-label");


        borderPane = new BorderPane();
        borderPane.setTop(paramName);
        borderPane.setCenter(slider);
        borderPane.setBottom(paramField);
        BorderPane.setAlignment(paramField, Pos.CENTER);
        BorderPane.setAlignment(paramName,Pos.CENTER);
        BorderPane.setAlignment(slider,Pos.CENTER);
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
