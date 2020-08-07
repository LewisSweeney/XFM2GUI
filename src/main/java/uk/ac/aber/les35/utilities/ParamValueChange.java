package uk.ac.aber.les35.utilities;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import jssc.SerialPortException;
import uk.ac.aber.les35.externalcode.IntField;
import uk.ac.aber.les35.serial.SerialCommandHandler;

import java.io.IOException;

public class ParamValueChange {
    static SerialCommandHandler serialCommandHandler;
    static IntField i;

    public static void onSliderChange(int p, IntField i, Slider s) {
        i.setValue(p);
    }

    public static void onFieldChange(int p, IntField i, Slider s, CheckBox[] c) throws SerialPortException, InterruptedException, IOException {
        s.setValue(p);
        ParamValueChange.i = i;
        CheckBox[] temp = new CheckBox[8];


        if (i.isBitwise()) {
            int j = 1;
            int sub = 1;
            if (c.length == 6) {
                j = 2;
                sub = 2;
            }

            String s1 = String.format("%8s", Integer.toBinaryString(i.getValue() & 0xFF)).replace(' ', '0');

            for (int k = 0; j < 8; j++) {
                if (s1.charAt(j) == '0') {
                    c[j - sub].setSelected(false);
                } else {
                    c[j - sub].setSelected(true);
                }
            }
        }
        if (serialCommandHandler.getLIVE_CHANGES()) {
            serialCommandHandler.setIndividualValue(Integer.parseInt(i.getId()), i.getValue());
        }
    }

    public static void setSerialCommandHandler(SerialCommandHandler serialCommandHandler) {
        ParamValueChange.serialCommandHandler = serialCommandHandler;
    }

}
