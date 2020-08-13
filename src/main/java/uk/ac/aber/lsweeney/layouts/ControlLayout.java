package uk.ac.aber.lsweeney.layouts;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jssc.SerialPortException;
import uk.ac.aber.lsweeney.externalcode.IntField;
import uk.ac.aber.lsweeney.enums.CONTROL_TYPE;
import uk.ac.aber.lsweeney.functionhandlers.ParamValueChangeHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This class creates the layout for each individual parameter control
 * Creates different control depending on passed parameters, but always creates an IntField to store parameter values,
 * even if not visible
 */
public class ControlLayout {
    // private final Integer paramValue = 0;
    IntField paramField = new IntField(0, 255, 0);
    Label paramName = new Label("DEFAULT");
    BorderPane layoutBorder;
    boolean bitwise = false;

    CONTROL_TYPE control_type;
    CheckBox[] bitwiseCheckboxes;
    Label[] labels;
    String[] paramSplit;

    ComboBox<String> waves;
    ArrayList<Image> images;

    ImageView waveImage;

    Slider slider = new Slider();

    RadioButton[] radioButtons = new RadioButton[2];

    public ControlLayout(String p) {
        paramSplit = p.split(":");
        paramName.getStyleClass().add("param-label");
        paramName.setText(paramSplit[0].toUpperCase());
        paramName.setAlignment(Pos.CENTER);
        paramName.setPrefWidth(100);
        paramField.setId(paramSplit[1]);


        paramField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ParamValueChangeHandler.onFieldChange(this);
            } catch (SerialPortException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });

        if (paramSplit.length > 2) {
            switch (paramSplit[2]) {
                case "BIT" -> bitwiseControl(paramSplit);
                case "WAVE" -> waveControl();
                case "TOGGLE" -> switchControl();
                default -> sliderControl(paramSplit);
            }
        } else {
            sliderControl(paramSplit);
        }
    }

    /**
     * Creates a control with a combobox and imageview to display the wave shape of the currently selected value
     */
    private void waveControl() {
        paramField = new IntField(0, 7, 0);
        paramField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ParamValueChangeHandler.onFieldChange(this);
            } catch (SerialPortException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        paramField.setId(paramSplit[1]);

        waves = new ComboBox<>();
        images = new ArrayList<>();

        waveImage = new ImageView();

        control_type = CONTROL_TYPE.WAVE;

        for (int i = 0; i < 8; i++) {
            images.add(new Image(String.valueOf(getClass().getResource("/images/waves/w" + i + ".png"))));
            waves.getItems().add("Shape " + (i + 1));
        }

        waves.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> onWaveSelected(waves.getSelectionModel().getSelectedIndex())
        );
        waves.getSelectionModel().selectFirst();


        waveImage.setImage(images.get(0));
        waveImage.setFitHeight(80);
        waveImage.setFitWidth(100);

        VBox imageBox = new VBox(waveImage);
        imageBox.getStyleClass().add("wave-image");

        layoutBorder = new BorderPane();
        layoutBorder.setTop(paramName);
        layoutBorder.setCenter(imageBox);
        layoutBorder.setBottom(waves);
        BorderPane.setAlignment(paramName, Pos.CENTER);
        BorderPane.setAlignment(waves, Pos.CENTER);

    }

    /**
     * Creates a bitwise control with relevant numbers of checkboxes
     * @param paramSplit
     */
    private void bitwiseControl(String[] paramSplit) {
        VBox[] checkAndLabel = new VBox[Integer.parseInt(paramSplit[3])];
        bitwise = true;
        control_type = CONTROL_TYPE.BITWISE;
        labels = new Label[Integer.parseInt(paramSplit[3])];
        bitwiseCheckboxes = new CheckBox[Integer.parseInt(paramSplit[3])];

        String[] labelSplit = paramSplit[4].split("/");

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(labelSplit[i]);
            bitwiseCheckboxes[i] = new CheckBox();
        }

        int bitNums = Integer.parseInt(paramSplit[3]);
        paramField.setBitwise(true);
        for (int i = 0; i < bitNums; i++) {
            checkAndLabel[i] = new VBox(labels[i], bitwiseCheckboxes[i]);
            checkAndLabel[i].getStyleClass().add("check-layout");
            bitwiseCheckboxes[i].setOnAction(e -> onCheckChange(Integer.parseInt(paramSplit[3])));
        }

        ArrayList<VBox> reversedCollection = new ArrayList<>(Arrays.asList(checkAndLabel));
        Collections.reverse(reversedCollection);

        HBox setOfBoxes = new HBox();
        setOfBoxes.getChildren().addAll(reversedCollection);
        setOfBoxes.getStyleClass().add("check-groups");

        layoutBorder = new BorderPane();
        layoutBorder.setTop(paramName);
        layoutBorder.setCenter(setOfBoxes);
        BorderPane.setAlignment(paramName, Pos.CENTER);
        BorderPane.setAlignment(setOfBoxes, Pos.CENTER);
    }

    /**
     * Creates a control that has a slider and a visible IntField to control parameter values
     * @param paramSplit
     */
    private void sliderControl(String[] paramSplit) {

        slider.setMin(0);
        slider.setMax(255);

        control_type = CONTROL_TYPE.SLIDER;

        if (paramSplit.length > 2) {
            slider.setMax(Double.parseDouble(paramSplit[2]));
            paramField = new IntField(0, Integer.parseInt(paramSplit[2]), 0);
            paramField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    ParamValueChangeHandler.onFieldChange(this);
                } catch (SerialPortException | InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            });

        }
        paramField.setMaxWidth(45);
        paramField.setAlignment(Pos.CENTER);
        paramField.setId(paramSplit[1]);

        slider.getStyleClass().add("slider-new");
        slider.setMaxWidth(80);

        slider.valueProperty().addListener((observableValue, number, t1) -> ParamValueChangeHandler.onSliderChange(this));

        layoutBorder = new BorderPane();
        layoutBorder.setTop(paramName);
        layoutBorder.setCenter(slider);
        layoutBorder.setBottom(paramField);
        BorderPane.setAlignment(paramField, Pos.CENTER);
    }

    /**
     * Creates a control that has an on/off toggle with radio buttons
     */
    private void switchControl() {

        paramField = new IntField(0, 1, 0);
        paramField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ParamValueChangeHandler.onFieldChange(this);
            } catch (SerialPortException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        paramField.setId(paramSplit[1]);

        control_type = CONTROL_TYPE.TOGGLE;
        ToggleGroup toggleGroup = new ToggleGroup();

        Label mode0Label = new Label("0");
        radioButtons[0] = new RadioButton();
        radioButtons[0].setToggleGroup(toggleGroup);
        VBox mode0Box = new VBox(mode0Label,radioButtons[0]);
        mode0Box.getStyleClass().add("check-layout");

        Label mode1Label = new Label("1");
        radioButtons[1] = new RadioButton();
        radioButtons[1].setToggleGroup(toggleGroup);
        VBox mode1Box = new VBox(mode1Label,radioButtons[1]);
        mode1Box.getStyleClass().add("check-layout");

        HBox radioButtonBox = new HBox(mode0Box,mode1Box);
        radioButtonBox.getStyleClass().add("check-groups");
        radioButtonBox.setStyle("-fx-alignment: center");

        layoutBorder = new BorderPane();
        layoutBorder.setTop(paramName);
        layoutBorder.setCenter(radioButtonBox);
        BorderPane.setAlignment(paramName, Pos.CENTER);
        BorderPane.setAlignment(radioButtonBox, Pos.CENTER);

        EventHandler<ActionEvent> mode0EventHandler = actionEvent -> onToggle(false);
        radioButtons[0].setOnAction(mode0EventHandler);

        EventHandler<ActionEvent> mode1EventHandler = actionEvent -> onToggle(true);
        radioButtons[1].setOnAction(mode1EventHandler);

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

    /**
     * Changes the value in the hidden paramfield depending on the radio button that's selected
     * @param selected The true or false state passed from the method
     */
    private void onToggle(boolean selected) {
        if(selected){
            paramField.setValue(1);
        } else{
            paramField.setValue(0);
        }
    }

    /**
     * Changes the image and parameter value for the relevant wave control
     * @param index index of selected item in combobox
     */
    private void onWaveSelected(int index) {
        paramField.setValue(index);
        waveImage.setImage(images.get(index));
    }

    // -GETTERS AND SETTERS-
    public BorderPane getLayout() {
        return layoutBorder;
    }

    public IntField getParamField() {
        return paramField;
    }

    public CONTROL_TYPE getControl_type() {
        return control_type;
    }

    public CheckBox[] getBitwiseCheckboxes() {
        return bitwiseCheckboxes;
    }

    public ComboBox<String> getWaves() {
        return waves;
    }

    public Slider getSlider() {
        return slider;
    }

    public ImageView getWaveImage() {
        return waveImage;
    }

    public RadioButton[] getRadioButtons() {
        return radioButtons;
    }

    public void setWaveImage(int val) {
        waveImage.setImage(images.get(val));
    }
}
