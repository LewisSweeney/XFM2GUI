package main.java.tabconstructors;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.layouts.ControlGroupLayoutConstructor;

import java.util.ArrayList;

/**
 * Creates the nodes for the Modulation tab of the interface
 */
public class ModulationTabConstructor {
    VBox layout = new VBox();
    ArrayList<VBox> groups = new ArrayList<>();
    ArrayList<HBox> groupsOfGroups = new ArrayList<>();

    public ModulationTabConstructor() {

        groups.add(new ControlGroupLayoutConstructor(4, "/parameters/modulation/pitchlfo.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(4, "/parameters/modulation/amplfo.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(4, "/parameters/modulation/egbias.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(4, "/parameters/modulation/pitch.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(4, "/parameters/modulation/arpeggiator.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(8, "/parameters/modulation/perfcontrols.txt").getControlGroup());


        groupsOfGroups.add(new HBox(groups.get(2)));
        groupsOfGroups.add(new HBox(groups.get(0), groups.get(1)));
        groupsOfGroups.add(new HBox(groups.get(3), groups.get(4)));
        groupsOfGroups.add(new HBox(groups.get(5)));

        for (HBox h : groupsOfGroups) {
            h.setSpacing(10);
            h.setAlignment(Pos.CENTER);
        }


    }

    public VBox getLayout() {
        layout.getChildren().addAll(groupsOfGroups);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(15);
        return layout;
    }
}
