package main.java.tabconstructors;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.layouts.ControlGroupLayoutConstructor;
import main.java.layouts.ControlLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Creates the nodes for the effects tab of the interface
 */
public class EffectsTabConstructor {
    VBox layout = new VBox();
    ArrayList<VBox> groups = new ArrayList<>();
    ArrayList<HBox> groupsOfGroups = new ArrayList<>();


    public EffectsTabConstructor() {

        groups.add(new ControlGroupLayoutConstructor(1, "/parameters/effects/bitcrusher.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(4, "/parameters/effects/am.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(4, "/parameters/effects/chorusflanger.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(4, "/parameters/effects/decimator.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(5, "/parameters/effects/delay.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(1, "/parameters/effects/fxrouting.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(5, "/parameters/effects/phaser.txt").getControlGroup());
        groups.add(new ControlGroupLayoutConstructor(3, "/parameters/effects/reverb.txt").getControlGroup());

        groupsOfGroups.add(new HBox(groups.get(5),groups.get(3),groups.get(1),groups.get(0)));
        groupsOfGroups.add(new HBox(groups.get(4),groups.get(6)));
        groupsOfGroups.add(new HBox(groups.get(2),groups.get(7)));

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
