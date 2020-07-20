package main.java.layouts;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.java.utilities.IntField;

/**
 * This class creates the layout for each individual parameter control
 * Currently creates an IntField and a Label
 * TODO: Add knobs/sliders
 */
public class ControlLayout {
    private final Integer paramValue = 0;
    private IntField paramField = new IntField(0, 255,0);
    Label paramName = new Label("DEFAULT");

    VBox layout;
    public ControlLayout(String p){
        paramName.setText(p);
        paramName.setAlignment(Pos.CENTER);
        paramName.setPrefWidth(100);

        paramField.setMaxWidth(45);
        paramField.setAlignment(Pos.CENTER);



        layout = new VBox(paramField,paramName);
        layout.setAlignment(Pos.CENTER);
    }

    public VBox getLayout(){
        return layout;
    }
}
