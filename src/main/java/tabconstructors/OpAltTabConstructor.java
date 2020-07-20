package main.java.tabconstructors;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.layouts.ControlGroupLayoutConstructor;

import java.util.ArrayList;

public class OpAltTabConstructor {
    VBox layout = new VBox();
    ArrayList<VBox> groups = new ArrayList<>();
    String filePath = "/parameters/operators/op1.txt";

    public OpAltTabConstructor(OperatorNumber operatorNumber) {

        switch(operatorNumber) {
            case ONE -> filePath = "/parameters/operators/op1.txt";
            case TWO -> filePath = "/parameters/operators/op2.txt";
            case THREE -> filePath = "/parameters/operators/op3.txt";
            case FOUR -> filePath = "/parameters/operators/op4.txt";
            case FIVE -> filePath = "/parameters/operators/op5.txt";
            case SIX -> filePath = "/parameters/operators/op6.txt";
        }

        groups.add(new ControlGroupLayoutConstructor(8, filePath).getControlGroup());
    }


    public VBox getLayout() {
        layout.getChildren().addAll(groups);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);
        return layout;
    }
}
