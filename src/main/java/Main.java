package main.java;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import main.java.externalcode.IntField;
import main.java.serial.SerialCommandHandler;
import main.java.serial.UNIT_NUMBER;
import main.java.tabconstructors.*;
import main.java.utilities.*;

import java.io.*;
import java.util.*;

public class Main extends Application {

    private static final int BOTTOM_BUTTON_WIDTH = 150;

    private ArrayList<Tab> tabs;

    private BorderPane border;
    private TabPane tabPane;
    private Scene scene;

    private final ComboBox<String> serialPortPicker = new ComboBox<>();
    private final ComboBox<Integer> patchPicker = new ComboBox<>();
    private final ComboBox<String> midiChOnePicker = new ComboBox<>();
    private final ComboBox<String> midiChTwoPicker = new ComboBox<>();

    // boolean layering = false;

    private final Stage fileStage = new Stage();
    private Stage stage;
    String[] serialPortNameList = SerialPortList.getPortNames();
    SerialPort serialPort;
    SerialCommandHandler serialCommandHandler = new SerialCommandHandler(serialPort);

    Boolean LIVE_CHANGES = false;

    final static ArrayList<IntField> paramFields = new ArrayList<>();

    String style = this.getClass().getResource("/stylesheets/style.css").toExternalForm();

    @Override
    public void start(Stage primaryStage) throws IOException, SerialPortException {
        stage = primaryStage;

        initializeScene();
        ParamValueChange.setSerialCommandHandler(serialCommandHandler);

        LIVE_CHANGES = false;
        MenuEventHandlers.setAllIntFieldValues(serialCommandHandler.getAllValues());
        LIVE_CHANGES = true;

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("XFM2GUI");
        primaryStage.show();

        serialCommandHandler.setLIVE_CHANGES(true);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Initialises the BorderPane used as the main node for the GUI
    private void initializeScene() throws IOException, SerialPortException {

        System.out.println("PROGRAM STARTING");

        BorderPane topBorder = new BorderPane();
        border = new BorderPane();
        tabPane = new TabPane();

        initTabs();
        initButtons();
        initPortSelection();

        tabPane.setTabMinWidth(80);
        tabPane.setTabMaxWidth(80);
        tabPane.getTabs().addAll(tabs);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);


        border.setCenter(tabPane);
        topBorder.setCenter(border);
        topBorder.setMaxWidth(1100);
        topBorder.setMinWidth(1100);
        topBorder.setMaxHeight((700));
        topBorder.setMinHeight(700);

        BorderPane.setAlignment(tabPane, Pos.CENTER);

        scene = new Scene(topBorder);
        scene.getStylesheets().add(style);

        MenuEventHandlers.setParams(serialCommandHandler,paramFields,scene);


    }

    /* Initialises the buttons at the bottom of the page
     */
    private void initButtons() throws IOException, SerialPortException {
        VBox xfmButtons = new VBox();
        VBox localButtons = new VBox();
        VBox serialPortSelection = new VBox();

        Label xfmPatch = new Label("XFM2 Program #:");

        EventHandler<? super MouseEvent> saveXFMEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                MenuEventHandlers.onSaveToXFMPress(patchPicker);
            } catch (SerialPortException | IOException e) {
                e.printStackTrace();
            }
        };
        Button saveToXFM = new Button("Save To XFM2");
        saveToXFM.setOnMouseClicked(saveXFMEventHandler);

        ArrayList<Integer> vals = new ArrayList<>();
        for (int i = 1; i <= 127; i++) {
            vals.add(i);
        }

        patchPicker.getItems().addAll(vals);
        patchPicker.setOnAction(e -> {
            try {
                MenuEventHandlers.onPatchPicked(patchPicker.getValue());
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }
        });

        Tooltip readTooltip = new Tooltip("Reads the current state of the XFM2 device");
        Tooltip writeTooltip = new Tooltip("Writes the current parameters to the XFM2");
        Tooltip saveCurrentTooltip = new Tooltip("Save the current paramater set locally in an XFM2 file");
        Tooltip loadTooltip = new Tooltip("Load an XFM2 file and set the parameters");
        Tooltip reloadTooltip = new Tooltip("Move all tabs back to this main window, even if they're closed");

        Button read = new Button("Read XFM2");
        read.setTooltip(readTooltip);

        Button write = new Button("Write to XFM2");
        write.setTooltip(writeTooltip);

        Button saveCurrentPatch = new Button("Save Program");
        saveCurrentPatch.setTooltip(saveCurrentTooltip);

        Button loadPatch = new Button("Load Program");
        loadPatch.setTooltip(loadTooltip);

        //Button reloadTabs = new Button("Refresh Tabs");
        //reloadTabs.setTooltip(reloadTooltip);

        EventHandler<? super MouseEvent> readEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                MenuEventHandlers.onReadButtonPress();
            } catch (SerialPortException | IOException e) {
                e.printStackTrace();
            }

        };
        read.setOnMouseClicked(readEventHandler);

        EventHandler<? super MouseEvent> saveEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                MenuEventHandlers.onSaveButtonPress(fileStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        saveCurrentPatch.setOnMouseClicked(saveEventHandler);

        EventHandler<? super MouseEvent> loadEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                MenuEventHandlers.onLoadButtonPress(fileStage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        };
        loadPatch.setOnMouseClicked(loadEventHandler);

        EventHandler<? super MouseEvent> writeEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                MenuEventHandlers.onWriteButtonPress();
            } catch (SerialPortException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        write.setOnMouseClicked(writeEventHandler);

        // EventHandler<? super MouseEvent> reloadTabsEventHandler = (EventHandler<MouseEvent>) mouseEvent -> reloadTabs();
        // reloadTabs.setOnMouseClicked(reloadTabsEventHandler);

        // Sets titles of each button sub-section
        Label serialPortLabel = new Label("Serial Port:");
        serialPortLabel.getStyleClass().add("button-group-title");

        Image logo = new Image("/images/logo.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(100);
        logoView.setFitWidth(100);

        Label xfmButtonsLabel = new Label("XFM2 Controls");

        xfmButtonsLabel.getStyleClass().add("button-group-title");

        Label localButtonsLabel = new Label("Local Controls");
        localButtonsLabel.getStyleClass().add("button-group-title");

        Label subtitle = new Label("By Lewis Sweeney");

        // Sets preferred width of the serialPort Picker
        // TODO: Change this to CSS.
        serialPortPicker.setPrefWidth(BOTTOM_BUTTON_WIDTH);

        // Creates new sub-sub-section for the patch control
        VBox patchControl = new VBox(xfmPatch, patchPicker, saveToXFM);

        // Creates each subsection being added to buttonSet
        serialPortSelection = new VBox(serialPortLabel, serialPortPicker);
        xfmButtons.getChildren().addAll(xfmButtonsLabel, read, write, patchControl, initMidiControl());
        localButtons.getChildren().addAll(localButtonsLabel, saveCurrentPatch, loadPatch);

        // Assigns style classes for each required node.
        serialPortSelection.getStyleClass().add("button-row");
        patchControl.getStyleClass().add("button-row");
        xfmButtons.getStyleClass().add("button-row");
        localButtons.getStyleClass().add("button-row");
        subtitle.getStyleClass().add("app-subtitle");

        Hyperlink about = new Hyperlink("About");
        EventHandler<? super MouseEvent> aboutEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            AboutSceneConstructor asc = new AboutSceneConstructor();
            asc.showStage();
        };
        about.setOnMouseClicked(aboutEventHandler);

        VBox buttonSet = new VBox(logoView, about, xfmButtons, localButtons, serialPortSelection);

        buttonSet.getStyleClass().add("button-column");

        border.setLeft(buttonSet);
    }

    private VBox initMidiControl() {
        for (int i = 0; i < 17; i++) {
            if (i == 0) {
                midiChOnePicker.getItems().add("All");
                midiChTwoPicker.getItems().add("All");
            } else {
                midiChOnePicker.getItems().add(String.valueOf(i));
                midiChTwoPicker.getItems().add(String.valueOf(i));
            }
        }

        midiChOnePicker.getStyleClass().add("channel-combo");
        midiChTwoPicker.getStyleClass().add("channel-combo");

        midiChOnePicker.setOnAction(e -> {
            try {
                int val = midiChOnePicker.getSelectionModel().getSelectedIndex();
                MenuEventHandlers.onMidiChannelChange(UNIT_NUMBER.ZERO,val);
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }

        });

        midiChTwoPicker.setOnAction(e -> {
            try {
                int val = midiChOnePicker.getSelectionModel().getSelectedIndex();
               MenuEventHandlers.onMidiChannelChange(UNIT_NUMBER.ONE,val);
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }

        });

        ToggleGroup unitGroup = new ToggleGroup();

        RadioButton unit0 = new RadioButton();
        RadioButton unit1 = new RadioButton();

        unit0.setToggleGroup(unitGroup);
        unit0.setSelected(true);
        unit1.setToggleGroup(unitGroup);

        EventHandler<ActionEvent> unitEventHandler = (EventHandler<ActionEvent>) mouseEvent -> {
            try {
                if(unit0.isSelected()){
                    MenuEventHandlers.setUnit(UNIT_NUMBER.ZERO);
                } else{
                    MenuEventHandlers.setUnit(UNIT_NUMBER.ONE);
                }
            } catch (SerialPortException | IOException e) {
                e.printStackTrace();
            }
        };

        unit0.setOnAction(unitEventHandler);
        unit1.setOnAction(unitEventHandler);


        Label midiTitle = new Label("Unit and MIDI Controls:");

        Label midiOne = new Label("Unit 0");
        Tooltip midiOneTooltip = new Tooltip("Set the MIDI channel for Unit 1 on the device");
        VBox midiConOne = new VBox(midiOne,unit0, midiChOnePicker);
        midiConOne.getStyleClass().add("picker-set");
        midiOne.setTooltip(midiOneTooltip);

        Label midiTwo = new Label("Unit 1");
        Tooltip midiTwoTooltip = new Tooltip("Set the MIDI channel for Unit 2 on the device");
        VBox midiConTwo = new VBox(midiTwo, unit1, midiChTwoPicker);
        midiConTwo.getStyleClass().add("picker-set");
        midiTwo.setTooltip(midiTwoTooltip);

        HBox midiCons = new HBox(midiConOne, midiConTwo);

        CheckBox layering = new CheckBox("Layering");
        Tooltip layeringTooltip = new Tooltip("Toggle layering on the XFM2");
        layering.setTooltip(layeringTooltip);
        layering.setOnAction(e ->{
            try{
                serialCommandHandler.setMidiLayering(layering.isSelected());
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }
        });

        VBox layout = new VBox(midiTitle, midiCons, layering);
        layout.getStyleClass().add("picker-row");


        return layout;
    }

    private void initPortSelection() throws IOException, SerialPortException {
        if (serialPortNameList.length > 0) {
            for (String s : serialPortNameList) {
                SerialPort sP = new SerialPort(s);
                serialCommandHandler.setSerialPort(sP);
                if (serialCommandHandler.getAllValues().length == 512) {
                    serialPort = new SerialPort(s);
                }
            }

            serialCommandHandler.setSerialPort(serialPort);
            for (String s : serialPortNameList) {
                serialPortPicker.getItems().add(s);
            }

            serialPortPicker.getSelectionModel().clearSelection();
            serialPortPicker.getSelectionModel().select(serialPort.getPortName());
        } else {
            serialPort = null;
            serialPortPicker.getItems().add("NO PORTS");
            serialPortPicker.getSelectionModel().selectFirst();
        }

        serialPortPicker.setOnAction(e -> {
            try {
                MenuEventHandlers.onSerialPortSelection(serialPortNameList,serialPortPicker,serialPort);
            } catch (SerialPortException serialPortException) {
                serialPortException.printStackTrace();
            }

        });
    }

    // Initialises the previously created tabs with the relevant nodes/content
    private void initTabs() {

        TabInit pathArrayLists = new TabInit();
        tabs = pathArrayLists.getTabs(paramFields);

    }






}
