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
import com.github.steadiestllama.xfm2gui.controls.ParameterControl;
import com.github.steadiestllama.xfm2gui.externalcode.IntField;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.github.steadiestllama.xfm2gui.controls.BitwiseControl;
import com.github.steadiestllama.xfm2gui.controls.WaveControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Groups together individual ControlLayouts into easily manageable VBox nodes
 */
public class ControlGroupLayoutConstructor {

    Label groupTitle = new Label("DEFAULT");
    ArrayList<ParameterControl> controls = new ArrayList<>();
    VBox controlGroup = new VBox();
    BufferedReader bReader;
    int rowLength;

    ControlLayoutFactory controlLayoutFactory = ControlLayoutFactory.getSingleInstance();

    // Constructor that takes a list of controls and a row length so it can construct a control group based on that
    public ControlGroupLayoutConstructor(int rowLength, String filepath) {
        this.rowLength = rowLength;
        ArrayList<HBox> rows = new ArrayList<>();
        ArrayList<HBox> bitRows = new ArrayList<>();
        ArrayList<HBox> wavRows = new ArrayList<>();
        bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filepath)));
        ArrayList<String> parameterStrings = new ArrayList<>();

        try {
            String line = bReader.readLine();
            while (line != null) {
                if (line.charAt(0) == '-') {
                    String replace = line.replace("-", "");
                    groupTitle.setText(replace);
                } else if (line.charAt(0) != '#') {
                    parameterStrings.add(line);
                }
                line = bReader.readLine();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        for (String p : parameterStrings) {
            controls.add(controlLayoutFactory.createControl(p));
        }


        if (rowLength < 1) {
            return;
        }

        int currentColumn = 0;
        int currentRow = 0;

        int bitRowLength = rowLength / 2;
        int currentBitCol = 0;
        int currentBitRow = 0;

        int currentWavCol = 0;
        int currentWavRow = 0;


        for (ParameterControl con : controls) {

            if (con instanceof WaveControl) {
                if (currentWavCol >= rowLength) {
                    currentWavCol = 0;
                    currentWavRow++;
                }
                if (currentWavCol == 0) {
                    HBox h = new HBox();
                    h.setAlignment(Pos.CENTER);
                    wavRows.add(h);
                }
                wavRows.get(currentWavRow).getChildren().add(con.getLayout());
                wavRows.get(currentWavRow).setStyle("-fx-padding: 0 0 10 0");
                currentWavCol++;
            } else if (con instanceof BitwiseControl) {
                if (currentBitCol >= bitRowLength) {
                    currentBitCol = 0;
                    currentBitRow++;
                }
                if (currentBitCol == 0) {
                    HBox h = new HBox();
                    h.setAlignment(Pos.CENTER);
                    bitRows.add(h);
                }
                bitRows.get(currentBitRow).getChildren().add(con.getLayout());
                bitRows.get(currentBitRow).setStyle("-fx-padding: 0 0 10 0");
                currentBitCol++;
            } else {
                if (currentColumn >= rowLength) {
                    currentColumn = 0;
                    currentRow++;
                }
                if (currentColumn == 0) {
                    HBox h = new HBox();
                    h.setAlignment(Pos.CENTER);
                    rows.add(h);
                }
                rows.get(currentRow).getChildren().add(con.getLayout());
                rows.get(currentRow).setStyle("-fx-padding: 0 0 10 0");
                currentColumn++;
            }


        }

        groupTitle.getStyleClass().add("group-label");

        controlGroup.getStyleClass().add("group-control");
        controlGroup.getChildren().add(groupTitle);
        controlGroup.getChildren().addAll(bitRows);
        controlGroup.getChildren().addAll(wavRows);
        controlGroup.getChildren().addAll(rows);

    }

    // GETTERS

    public VBox getControlGroup() {
        return controlGroup;
    }

 /*   public HBox getOperatorLinkBoxes(OPERATOR_NUM opNum) {
        VBox layout = new VBox();
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        ArrayList<Label> checkBoxLabels = new ArrayList<>();
        ArrayList<Boolean> checkBoxActive = new ArrayList<>();

        Label title = new Label("Link Operators");
        title.getStyleClass().add("group-label");
        layout.getChildren().add(title);
        for (int i = 0; i <= 5; i++) {
            checkBoxes.add(new CheckBox());
            int num = i + 1;
            checkBoxLabels.add(new Label("Operator " + num));
            checkBoxActive.add(false);
        }

        int currentOp;

        switch (opNum) {

            case op1 -> currentOp = 1;

            case op2 -> currentOp = 2;

            case op3 -> currentOp = 3;

            case op4 -> currentOp = 4;

            case op5 -> currentOp = 5;

            case op6 -> currentOp = 6;

            default -> throw new IllegalStateException("Unexpected value: " + opNum);
        }

        for (int i = 0; i <= 5; i++) {
            if (i + 1 != currentOp) {
                HBox checkAndLabel = new HBox(checkBoxes.get(i), checkBoxLabels.get(i));
                checkAndLabel.getStyleClass().add("check-layout");
                layout.getChildren().add(checkAndLabel);
            }
        }

        layout.getStyleClass().add("check-group");
        return new HBox(layout);
    } */

    public ArrayList<IntField> getIntFields() {
        ArrayList<IntField> intFields = new ArrayList<>();
        for (ParameterControl c : controls) {
            intFields.add(c.getParamField());
        }
        return intFields;
    }
}
