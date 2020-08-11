package uk.ac.aber.les35.functionhandlers;

import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import jssc.SerialPortException;
import uk.ac.aber.les35.enums.CONTROL_TYPE;
import uk.ac.aber.les35.layouts.ControlLayout;

import java.io.IOException;

/**
 * This class is in charge of handling any value changes between elements of a ControlLayout, as well as
 * sending commands to the SerialCommandHandler, to be executed if Live Changes are enabled
 */
public class ParamValueChangeHandler {
    static SerialCommandHandler serialCommandHandler;

    /**
     * When the slider value is changed this method ensures the parameter IntField is also updated
     *
     * @param c The ControlLayout being used for inspecting nodes and values
     */
    public static void onSliderChange(ControlLayout c) {
        c.getParamField().setValue((int) c.getSlider().getValue());
    }

    /**
     * When the param IntField is changed this method ensures any other relevant controls are updated, as well as
     * sending command to the SerialCommandHandler
     *
     * @param c The ControlLayout being used for inspecting nodes and values
     * @throws SerialPortException
     * @throws InterruptedException
     * @throws IOException
     */
    public static void onFieldChange(ControlLayout c) throws SerialPortException, InterruptedException, IOException {
        Slider s = c.getSlider();
        s.setValue(c.getParamField().getValue());
        if(c.getParamField().getValue() > s.getMax()){
            s.setValue(s.getMax());
        }

        if (c.getControl_type() == CONTROL_TYPE.BITWISE) {
            bitwiseChange(c);
        } else if (c.getControl_type() == CONTROL_TYPE.WAVE) {
            waveChange(c);
        } else if (c.getControl_type() == CONTROL_TYPE.TOGGLE) {
            toggleChange(c);
        }
        if (serialCommandHandler != null) {
            if (serialCommandHandler.getLIVE_CHANGES()) {
                serialCommandHandler.setIndividualValue(Integer.parseInt(c.getParamField().getId()), c.getParamField().getValue());
            }
        }
    }

    /**
     * Specifically called when the controllayout being worked on is of BITWISE type
     *
     * @param c The ControlLayout being used for inspecting nodes and values
     */
    private static void bitwiseChange(ControlLayout c) {
        int j = 1;
        int sub = 1;
        CheckBox[] bitwiseCheck = c.getBitwiseCheckboxes();
        if (bitwiseCheck.length == 6) {
            j = 2;
            sub = 2;
        }

        String s1 = String.format("%8s", Integer.toBinaryString(c.getParamField().getValue() & 0xFF)).replace(' ', '0');

        for (int k = 0; j < 8; j++) {
            bitwiseCheck[j - sub].setSelected(s1.charAt(j) != '0');
        }
    }

    /**
     * Specifically called when the controllayout being worked on is of WAVE type
     *
     * @param c The ControlLayout being used for inspecting nodes and values
     */
    private static void waveChange(ControlLayout c) {
        int val = c.getParamField().getValue();

        if (val < 0) {
            val = 0;
        } else if (val > 7) {
            val = 7;
        }

        c.getWaves().getSelectionModel().select(val);
        c.setWaveImage(c.getWaves().getSelectionModel().getSelectedIndex());
    }

    /**
     * Specifically called when the controllayout being worked on is of TOGGLE type
     *
     * @param c The ControlLayout being used for inspecting nodes and values
     */
    private static void toggleChange(ControlLayout c) {
        RadioButton[] radioButtons = c.getRadioButtons();

        if (c.getParamField().getValue() > 0) {
            radioButtons[0].setSelected(false);
            radioButtons[1].setSelected(true);
        } else {
            radioButtons[1].setSelected(false);
            radioButtons[0].setSelected(true);
        }
    }

    /**
     * Used to change the SerialCommandHandler being used to send serial commands
     *
     * @param serialCommandHandler SerialCommandHandler being passed in for use
     */
    public static void setSerialCommandHandler(SerialCommandHandler serialCommandHandler) {
        ParamValueChangeHandler.serialCommandHandler = serialCommandHandler;
    }

}
