package main.java.tabconstructors;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.layouts.ControlGroupLayoutConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Creates the nodes for the program tab of the interface
 */
public class TabConstructor {



    public VBox getLayout(ArrayList<String> filepaths, ArrayList<String> groupValues) {
        ArrayList<ControlGroupLayoutConstructor> groups = new ArrayList<>();
        ArrayList<HBox> groupsOfGroups = new ArrayList<>();
        VBox layout = new VBox();
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

            ControlGroupLayoutConstructor cgl = new ControlGroupLayoutConstructor(rowLengths, filepath);
            groups.add(cgl);
        }

        for(String s:groupValues){
            String[] split = s.split(":");
            HBox h = new HBox();
            h.getStyleClass().add("group-groups");
            for (String value : split) {
                h.getChildren().add(groups.get(Integer.parseInt(value)).getControlGroup());
            }
            groupsOfGroups.add(h);
        }

        layout.getChildren().addAll(groupsOfGroups);
        layout.getStyleClass().add("tab-layout");
        return layout;
    }
}
