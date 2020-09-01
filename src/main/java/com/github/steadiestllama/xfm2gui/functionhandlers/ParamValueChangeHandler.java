package com.github.steadiestllama.xfm2gui.functionhandlers;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import com.github.steadiestllama.xfm2gui.serial.SerialHandlerBridge;

import java.io.IOException;

/**
 * This class is in charge of handling any value changes between elements of a ControlLayout, as well as
 * sending commands to the SerialCommandHandler, to be executed if Live Changes are enabled
 */
public class ParamValueChangeHandler {


    static SerialHandlerBridge serialHandler;
    private static final OptionsHandler optionsHandler = OptionsHandler.getSingleInstance();
    private static final MenuEventHandler menuEventHandler = MenuEventHandler.getSingleInstance();

    /**
     * When the slider value is changed this method ensures the parameter IntField is also updated
     *
     * @param c The ControlLayout being used for inspecting nodes and values
     */
    public static void onSliderChange(ParameterControl c) {
        c.getParamField().setValue((int) c.getSlider().getValue());
    }

    /**
     * When the param IntField is changed this method ensures any other relevant controls are updated, as well as
     * sending command to the SerialCommandHandler
     *
     * @param c The ControlLayout being used for inspecting nodes and values

     * @throws IOException Never thrown but IntelliJ keeps threatening me whenever I try to remove it
     */
    @SuppressWarnings("RedundantThrows")
    public static void onFieldChange(ParameterControl c) throws IOException {

                if (c instanceof BitwiseControl) {
                    bitwiseChange(c);
                } else if (c instanceof WaveControl) {
                    waveChange(c, c.getWaves());
                } else if (c instanceof ToggleControl) {
                    toggleChange(c);
                } else if (c instanceof SliderControl) {
                    sliderChange(c);
                }

        Runnable r = () -> {
            if (serialHandler != null) {
                if (optionsHandler.getLiveChanges()) {
                    try {
                        Thread.sleep(5);
                        menuEventHandler.onWriteButtonPress();
                    } catch (IOException | InterruptedException ignored) {
                    }
                }
            }
        };

        new Thread(r).start();


    }

    private static void sliderChange(ParameterControl c) {
        Slider s = c.getSlider();
        s.setValue(c.getParamField().getValue());
        if (c.getParamField().getValue() > s.getMax()) {
            s.setValue(s.getMax());
        }
    }

    /**
     * Specifically called when the control layout being worked on is of BITWISE type
     *
     * @param c The ControlLayout being used for inspecting nodes and values
     */
    private static void bitwiseChange(ParameterControl c) {
        int j = 1;
        int sub = 1;
        CheckBox[] bitwiseCheck = c.getBitwiseCheckboxes();
        if (bitwiseCheck.length > 1) {
            if (bitwiseCheck.length == 6) {
                j = 2;
                sub = 2;
            }

            String s1 = String.format("%8s", Integer.toBinaryString(c.getParamField().getValue() & 0xFF)).replace(' ', '0');

            for (; j < 8; j++) {
                bitwiseCheck[j - sub].setSelected(s1.charAt(j) != '0');
            }
        }

    }

    /**
     * Specifically called when the controllayout being worked on is of WAVE type
     *
     * @param c The ControlLayout being used for inspecting nodes and values
     */
    private static void waveChange(ParameterControl c, ComboBox<String> waves) {
        int val = c.getParamField().getValue();

        if (val < 0) {
            val = 0;
        } else if (val > 7) {
            val = 7;
        }
        if (c.getWaves() != null) {
            waves.getSelectionModel().select(val);
            c.setWaveImage(c.getWaves().getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Specifically called when the controllayout being worked on is of TOGGLE type
     *
     * @param c The ControlLayout being used for inspecting nodes and values
     */
    private static void toggleChange(ParameterControl c) {
        RadioButton[] radioButtons = c.getRadioButtons();
        if (radioButtons != null) {
            if (c.getParamField().getValue() > 0) {
                radioButtons[0].setSelected(false);
                radioButtons[1].setSelected(true);
            } else {
                radioButtons[1].setSelected(false);
                radioButtons[0].setSelected(true);
            }
        }
    }

    /**
     * Used to change the SerialCommandHandler being used to send serial commands
     *
     * @param serialHandler SerialHandlerBridge being passed in for use
     */
    public static void setSerialHandler(SerialHandlerBridge serialHandler) {
        ParamValueChangeHandler.serialHandler = serialHandler;
    }

}
