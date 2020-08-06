package main.java.utilities;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import jssc.SerialPortException;
import main.java.externalcode.IntField;
import main.java.serial.SerialCommandHandler;

import java.io.IOException;

public class ParamValueChange {
    static SerialCommandHandler serialCommandHandler;
    static IntField i;
    public static void onSliderChange(int p, IntField i, Slider s){
        i.setValue(p);
    }

    public static void onFieldChange(int p, IntField i, Slider s, CheckBox[] c) throws SerialPortException, InterruptedException, IOException {
        s.setValue(p);
        ParamValueChange.i = i;
        if(i.isBitwise()){
            String s1 = String.format("%8s", Integer.toBinaryString(i.getValue() & 0xFF)).replace(' ', '0');
            for(int j = 1;j<8;j++){
                if(s1.charAt(j) == '0'){
                    c[j-1].setSelected(false);
                } else{
                    c[j-1].setSelected(true);
                }
            }
        }
        if(serialCommandHandler.getLIVE_CHANGES()){
            serialCommandHandler.setIndividualValue(Integer.parseInt(i.getId()),i.getValue());
        }
    }

    public static void setSerialCommandHandler(SerialCommandHandler serialCommandHandler){
        ParamValueChange.serialCommandHandler = serialCommandHandler;
    }

}
