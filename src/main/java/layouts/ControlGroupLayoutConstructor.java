package main.java.layouts;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.externalcode.IntField;
import main.java.utilities.OPERATOR_NUM;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Groups together individual ControlLayouts into easily manageable VBox nodes
 */
public class ControlGroupLayoutConstructor {

    Label groupTitle = new Label("DEFAULT");
    ArrayList<ControlLayout> controls = new ArrayList<>();
    VBox controlGroup = new VBox();
    BufferedReader bReader;
    int rowLength;

    // Constructor that takes a list of controls and a row length so it can construct a control group based on that
    public ControlGroupLayoutConstructor(int rowLength, String filepath) {
        this.rowLength = rowLength;
        ArrayList<HBox> rows = new ArrayList<>();
        bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filepath)));
        ArrayList<String> paramNames = new ArrayList<>();

        try {
            String line = bReader.readLine();
            while(line != null){
                if(line.charAt(0) == '-') {
                    String replace = line.replace("-", "");
                    groupTitle.setText(replace);
                }
                else if(line.charAt(0) != '#'){
                    paramNames.add(line);
                }
                line = bReader.readLine();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        for (String p : paramNames){
            ControlLayout controlLayout = new ControlLayout(p);
            controls.add(controlLayout);
        }


        if (rowLength < 1) {
            return;
        }

        int currentColumn = 0;
        int currentRow = 0;

        for (ControlLayout con : controls) {
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

        groupTitle.getStyleClass().add("group-label");
        controlGroup.getStyleClass().add("group-control");
        controlGroup.getChildren().add(groupTitle);
        controlGroup.getChildren().addAll(rows);
    }

    public VBox getControlGroup() {
        return controlGroup;
    }

    public HBox getOperatorLinkBoxes(OPERATOR_NUM opNum) {
        VBox layout = new VBox();
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        ArrayList<Label> checkBoxLabels = new ArrayList<>();
        ArrayList<Boolean> checkBoxActive = new ArrayList<>();

        Label title = new Label("Link Operators");
        title.getStyleClass().add("group-label");
        layout.getChildren().add(title);
        for (int i = 0; i <= 5; i++) {
            checkBoxes.add(new CheckBox());
            int num = i+1;
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
            if (i+1 != currentOp) {
                HBox checkAndLabel = new HBox(checkBoxes.get(i), checkBoxLabels.get(i));
                checkAndLabel.getStyleClass().add("check-layout");
                layout.getChildren().add(checkAndLabel);
            }
        }

        layout.getStyleClass().add("check-group");
        return new HBox(layout);
    }

    public ArrayList<IntField> getIntFields(){
        ArrayList<IntField> intFields = new ArrayList<>();
        for(ControlLayout cl:controls){
            intFields.add(cl.getParamField());
        }
        return intFields;
    }
}
