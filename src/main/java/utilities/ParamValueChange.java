package main.java.utilities;

import javafx.scene.control.Slider;
import jssc.SerialPortException;
import main.java.externalcode.IntField;
import main.java.serial.SerialCommandHandler;

import java.io.IOException;

public class ParamValueChange {
    static SerialCommandHandler serialCommandHandler;
    public static void onSliderChange(int p, IntField i, Slider s){
        i.setValue(p);
    }

    public static void onFieldChange(int p, IntField i, Slider s) throws SerialPortException, InterruptedException, IOException {
        s.setValue(p);
        if(serialCommandHandler.getLIVE_CHANGES()){
            serialCommandHandler.setIndividualValue(Integer.parseInt(i.getId()),i.getValue());
        }
    }

    public static void setSerialCommandHandler(SerialCommandHandler serialCommandHandler){
        ParamValueChange.serialCommandHandler = serialCommandHandler;
    }
}
