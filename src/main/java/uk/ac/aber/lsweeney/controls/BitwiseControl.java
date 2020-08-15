package uk.ac.aber.lsweeney.controls;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BitwiseControl extends ParameterControl {

    CheckBox[] bitwiseCheckboxes;
    Label[] labels;
    int bitNum;
    String boxLabels;

    public BitwiseControl(String name, String paramID, int maxVal, int bitNum, String boxLabels) {
        super(name, paramID, maxVal);
        this.bitNum = bitNum;
        this.boxLabels = boxLabels;

        constructLayout();
    }

    private void constructLayout(){
        VBox[] checkAndLabel = new VBox[bitNum];
        labels = new Label[bitNum];
        bitwiseCheckboxes = new CheckBox[bitNum];

        String[] labelSplit = boxLabels.split("/");

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(labelSplit[i]);
            bitwiseCheckboxes[i] = new CheckBox();
        }

        paramField.setBitwise(true);
        for (int i = 0; i < bitNum; i++) {
            checkAndLabel[i] = new VBox(labels[i], bitwiseCheckboxes[i]);
            checkAndLabel[i].getStyleClass().add("check-layout");
            bitwiseCheckboxes[i].setOnAction(e -> onCheckChange(bitNum));
        }

        ArrayList<VBox> reversedCollection = new ArrayList<>(Arrays.asList(checkAndLabel));
        Collections.reverse(reversedCollection);

        paramName.getStyleClass().add("param-label");

        HBox setOfBoxes = new HBox();
        setOfBoxes.getChildren().addAll(reversedCollection);
        setOfBoxes.getStyleClass().add("check-groups");

        borderPane = new BorderPane();
        borderPane.setTop(paramName);
        borderPane.setCenter(setOfBoxes);
        BorderPane.setAlignment(paramName, Pos.CENTER);
        BorderPane.setAlignment(setOfBoxes, Pos.CENTER);
        borderPane.getStyleClass().add("control");
    }

    @Override
    public BorderPane getLayout(){


        return borderPane;
    }

    @Override
    public CheckBox[] getBitwiseCheckboxes(){
        return bitwiseCheckboxes;
    }

    /**
     * Called when a bitwise checkbox has its state changed
     * @param bitNum Number of bits being used in the bitwise parameter checking
     */
    private void onCheckChange(int bitNum) {
        byte paramValue = 0;
        for (byte i = 0; i < bitNum; i++) {
            if (bitwiseCheckboxes[i].isSelected()) {
                byte add = 1;
                byte iter = (byte) (bitNum - i);
                for (byte j = 1; j < iter; j++) {
                    add = (byte) (add * 2);
                }
                paramValue = (byte) (paramValue + add);
            }
        }
        paramField.setValue(paramValue & 0xFF);
    }
}
