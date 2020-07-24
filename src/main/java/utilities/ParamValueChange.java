package main.java.utilities;

import javafx.scene.control.Slider;
import main.java.externalcode.IntField;

public class ParamValueChange {
    public static void onSliderChange(int p, IntField i, Slider s){
        i.setValue(p);
    }

    public static void onFieldChange(int p, IntField i, Slider s){
        s.setValue(p);
    }
}
