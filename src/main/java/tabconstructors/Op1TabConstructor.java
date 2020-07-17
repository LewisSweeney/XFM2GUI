package main.java.tabconstructors;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.layouts.ControlLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Op1TabConstructor {

    VBox layout = new VBox();
    ArrayList<String> paramNames = new ArrayList<>();
    ArrayList<ControlLayout> paramControlsTotal = new ArrayList<>();
    ArrayList<HBox> paramControlGroups = new ArrayList<>();
    BufferedReader bReader;


    public Op1TabConstructor() {
        bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/parameters/operators/op1.txt")));
        try {
            String line = bReader.readLine();
            while(line != null){
                paramNames.add(line);
                line = bReader.readLine();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        for (String p : paramNames){
            ControlLayout controlLayout = new ControlLayout(p);
            paramControlsTotal.add(controlLayout);
        }

        int groupSize = 0;
        int currentGroup = 0;

        for (ControlLayout c:paramControlsTotal){
            if(groupSize >= 10){
                groupSize = 0;
                currentGroup++;
            }
            if(groupSize == 0){
                HBox h = new HBox();
                paramControlGroups.add(h);
            }

            paramControlGroups.get(currentGroup).getChildren().add(c.getLayout());
            groupSize++;

        }
    }


    public VBox getLayout() {
        for(HBox h:paramControlGroups){
            h.setAlignment(Pos.CENTER);
        }
        layout.getChildren().addAll(paramControlGroups);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);
        return layout;
    }

}
