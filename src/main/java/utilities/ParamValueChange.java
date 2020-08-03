package main.java.utilities;

import javafx.scene.control.Slider;
import jssc.SerialPortException;
import main.java.externalcode.IntField;
import main.java.serial.SerialCommandHandler;

public class ParamValueChange {
    static SerialCommandHandler serialCommandHandler;
    public static void onSliderChange(int p, IntField i, Slider s){
        i.setValue(p);
    }

    public static void onFieldChange(int p, IntField i, Slider s) throws SerialPortException, InterruptedException {
        s.setValue(p);
        System.out.println("Changed value of param number " + i.getId());
    }

    public static void setSerialCommandHandler(SerialCommandHandler serialCommandHandler){
        ParamValueChange.serialCommandHandler = serialCommandHandler;
    }
}
