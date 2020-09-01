package com.github.steadiestllama.xfm2gui.layouts;

/*

This file is part of XFM2GUI

Copyright 2020 Lewis Sweeney

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

import com.github.steadiestllama.xfm2gui.externalcode.IntField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    public BorderPane getLayout(ArrayList<String> filepaths, ArrayList<String> groupValues) {
        groups = new ArrayList<>();
        groupsOfGroups = new ArrayList<>();
        VBox internalLayout = new VBox();
        BorderPane layout = new BorderPane();
        ControlGroupLayoutConstructor cgl;
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

        internalLayout.getStyleClass().add("tab-layout");
        return layout;
    }

    public ArrayList<IntField> getIntFields(){
        return intFields;
    }

}
