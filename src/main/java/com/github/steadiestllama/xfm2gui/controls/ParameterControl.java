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

import com.github.steadiestllama.xfm2gui.externalcode.IntField;
import com.github.steadiestllama.xfm2gui.functionhandlers.ParamValueChangeHandler;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

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
