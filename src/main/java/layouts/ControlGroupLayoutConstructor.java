package main.java.layouts;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Groups together individual ControlLayouts into easily manageable VBox nodes
 */
public class ControlGroupLayoutConstructor {
    Label groupTitle = new Label("DEFAULT");
    ArrayList<ControlLayout> controls = new ArrayList<>();
    VBox controlGroup = new VBox();
    BufferedReader bReader;

    // Constructor that takes a list of controls and a row length so it can construct a control group based on that
    public ControlGroupLayoutConstructor(int rowLength, String filepath) {
        ArrayList<HBox> rows = new ArrayList<>();
        bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filepath)));

        ArrayList<String> paramNames = new ArrayList<>();

        try {
            String line = bReader.readLine();
            while(line != null){
                if(line.charAt(0) == '-') {
                    groupTitle.setText(line);
                    line = bReader.readLine();
                } else {
                    paramNames.add(line);
                    line = bReader.readLine();
                }
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
        
        controlGroup.getChildren().add(groupTitle);
        controlGroup.getChildren().addAll(rows);
    }

    public VBox getControlGroup() {
        controlGroup.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return controlGroup;
    }


}
