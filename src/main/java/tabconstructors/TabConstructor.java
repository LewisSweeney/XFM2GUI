package main.java.tabconstructors;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.externalcode.IntField;
import main.java.layouts.ControlGroupLayoutConstructor;
import main.java.utilities.OPERATOR_NUM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Creates the nodes for the program tab of the interface
 */
public class TabConstructor {
    ArrayList<IntField> intFields = new ArrayList<>();
    ArrayList<ControlGroupLayoutConstructor> groups;
    ArrayList<HBox> groupsOfGroups;

    public BorderPane getLayout(ArrayList<String> filepaths, ArrayList<String> groupValues, OPERATOR_NUM opNum) {
        groups = new ArrayList<>();
        groupsOfGroups = new ArrayList<>();
        VBox internalLayout = new VBox();
        BorderPane layout = new BorderPane();
        ControlGroupLayoutConstructor cgl = null;
        for (String filepath : filepaths) {

            int rowLengths = 1;
            BufferedReader bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filepath)));
            try {
                String line = bReader.readLine();
                while (line != null) {
                    if (line.charAt(0) == '#') {
                        String replace = line.replace("#", "");
                        rowLengths = Integer.parseInt(replace);
                    }
                    line = bReader.readLine();
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            cgl = new ControlGroupLayoutConstructor(rowLengths, filepath);
            groups.add(cgl);
            intFields.addAll(cgl.getIntFields());
        }

        for (String s : groupValues) {
            String[] split = s.split(":");
            HBox h = new HBox();
            h.getStyleClass().add("group-groups");
            for (String value : split) {
                h.getChildren().add(groups.get(Integer.parseInt(value)).getControlGroup());
            }
            groupsOfGroups.add(h);
        }


        internalLayout.getChildren().addAll(groupsOfGroups);
        layout.setCenter(internalLayout);
        /*
        if (!opNum.equals(OPERATOR_NUM.no)) {
            assert cgl != null;
            layout.setLeft(cgl.getOperatorLinkBoxes(opNum));
        }
        */
        internalLayout.getStyleClass().add("tab-layout");
        return layout;
    }

    public ArrayList<IntField> getIntFields(){
        return intFields;
    }

}
