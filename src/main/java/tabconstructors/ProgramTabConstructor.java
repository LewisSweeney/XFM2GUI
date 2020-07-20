package main.java.tabconstructors;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.layouts.ControlGroupLayoutConstructor;

import java.util.ArrayList;

/**
 * Creates the nodes for the program tab of the interface
 */
public class ProgramTabConstructor {
    VBox layout = new VBox();
    ArrayList<VBox> groups = new ArrayList<>();
    ArrayList<HBox> groupsOfGroups = new ArrayList<>();

    public ProgramTabConstructor() {

        groups.add(new ControlGroupLayoutConstructor(8, "/parameters/program/pitcheg.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(6, "/parameters/program/lfo.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(6, "/parameters/program/other.txt").getControlGroup());

        groupsOfGroups.add(new HBox(groups.get(0)));
        groupsOfGroups.add(new HBox(groups.get(1)));
        groupsOfGroups.add(new HBox(groups.get(2)));

        for (HBox h:groupsOfGroups) {
            h.setSpacing(10);
            h.setAlignment(Pos.CENTER);
        }
    }


    public VBox getLayout() {
        layout.getChildren().addAll(groupsOfGroups);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        return layout;
    }
}
