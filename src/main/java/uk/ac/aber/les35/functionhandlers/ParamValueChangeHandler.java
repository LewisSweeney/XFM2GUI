package uk.ac.aber.les35.functionhandlers;

import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import jssc.SerialPortException;
import uk.ac.aber.les35.enums.CONTROL_TYPE;
import uk.ac.aber.les35.layouts.ControlLayout;

import java.io.IOException;

public class ParamValueChangeHandler {
    static SerialCommandHandler serialCommandHandler;

    public static void onSliderChange(ControlLayout c) {
        c.getParamField().setValue((int) c.getSlider().getValue());
    }

    public static void onFieldChange(ControlLayout c) throws SerialPortException, InterruptedException, IOException {
        Slider s = c.getSlider();
        s.setValue(c.getParamField().getValue());
        CheckBox[] temp = new CheckBox[8];

        if (c.getControl_type() == CONTROL_TYPE.BITWISE) {
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
        } else if(c.getControl_type() == CONTROL_TYPE.WAVE){
            int val = c.getParamField().getValue();

            if(val < 0){
                val = 0;
            } else if(val > 7){
                val = 7;
            }

            c.getWaves().getSelectionModel().select(val);
            c.setWaveImage(c.getWaves().getSelectionModel().getSelectedIndex());
        } else if(c.getControl_type() == CONTROL_TYPE.TOGGLE){
            RadioButton[] radioButtons = c.getRadioButtons();

            if(c.getParamField().getValue() > 0){
                radioButtons[0].setSelected(false);
                radioButtons[1].setSelected(true);
            } else{
                radioButtons[1].setSelected(false);
                radioButtons[0].setSelected(true);
            }
        }
        if (serialCommandHandler.getLIVE_CHANGES()) {
            serialCommandHandler.setIndividualValue(Integer.parseInt(c.getParamField().getId()), c.getParamField().getValue());
        }
    }

    public static void setSerialCommandHandler(SerialCommandHandler serialCommandHandler) {
        ParamValueChangeHandler.serialCommandHandler = serialCommandHandler;
    }

}
