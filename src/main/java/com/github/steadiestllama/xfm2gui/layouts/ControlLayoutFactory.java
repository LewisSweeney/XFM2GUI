package com.github.steadiestllama.xfm2gui.layouts;

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

import com.github.steadiestllama.xfm2gui.controls.*;

/**
 * This class creates the layout for each individual parameter control
 * Creates different control depending on passed parameters, but always creates an IntField to store parameter values,
 * even if not visible
 */
public class ControlLayoutFactory {
    String[] paramSplit;

    ParameterControl control;

    private static final ControlLayoutFactory SINGLE_INSTANCE = new ControlLayoutFactory();

    public ParameterControl createControl(String p){
        paramSplit = p.split(":");

        if (paramSplit.length > 2) {
            switch (paramSplit[2]) {
                case "BIT" -> bitwiseControl(paramSplit);
                case "WAVE" -> waveControl();
                case "TOGGLE" -> switchControl();
                default -> sliderControl(paramSplit);
            }
        } else {
            sliderControl(paramSplit);
        }

        return control;
    }
    /**
     * Creates a control with a combobox and imageview to display the wave shape of the currently selected value
     */
    private void waveControl() {
        control = new WaveControl(paramSplit[0], paramSplit[1], 7);
    }

    /**
     * Creates a bitwise control with relevant numbers of checkboxes
     *
     * @param paramSplit The split up parameter string passed into the constructor
     */
    private void bitwiseControl(String[] paramSplit) {
        control = new BitwiseControl(paramSplit[0], paramSplit[1], 255, Integer.parseInt(paramSplit[3]), paramSplit[4]);
    }

    /**
     * Creates a control that has a slider and a visible IntField to control parameter values
     *
     * @param paramSplit The split up parameter string passed into the constructor
     */
    private void sliderControl(String[] paramSplit) {
        if (paramSplit.length > 2) {
            control = new SliderControl(paramSplit[0], paramSplit[1], Integer.parseInt(paramSplit[2]));
        } else {
            control = new SliderControl(paramSplit[0], paramSplit[1], 255);
        }
    }

    /**
     * Creates a control that has an on/off toggle with radio buttons
     */
    private void switchControl() {
        control = new ToggleControl(paramSplit[0], paramSplit[1], 1);
    }

    public static ControlLayoutFactory getSingleInstance(){
        return SINGLE_INSTANCE;
    }
}
