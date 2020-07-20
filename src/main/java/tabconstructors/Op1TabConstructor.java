package main.java.tabconstructors;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.layouts.ControlGroupLayoutConstructor;
import main.java.layouts.ControlLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Op1TabConstructor {

    VBox layout = new VBox();
    ArrayList<VBox> groups = new ArrayList<>();
    ArrayList<HBox> groupsOfGroups = new ArrayList<>();

    public Op1TabConstructor() {
        groups.add(new ControlGroupLayoutConstructor(8, "/parameters/operators/op1.txt").getControlGroup());
    }


    public VBox getLayout() {
        layout.getChildren().addAll(groups);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);
        return layout;
    }

}
