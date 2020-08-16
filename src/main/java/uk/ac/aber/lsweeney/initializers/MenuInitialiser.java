package uk.ac.aber.lsweeney.initializers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import jssc.SerialPort;
import jssc.SerialPortException;

import uk.ac.aber.lsweeney.enums.UNIT_NUMBER;
import uk.ac.aber.lsweeney.externalcode.IntField;
import uk.ac.aber.lsweeney.functionhandlers.AlertHandler;
import uk.ac.aber.lsweeney.functionhandlers.MenuEventHandler;
import uk.ac.aber.lsweeney.functionhandlers.SerialCommandHandler;
import uk.ac.aber.lsweeney.sceneconstructors.AboutSceneConstructor;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Main class for creating the menu section of the GUI.
 * Has various methods that create, initialise, and set nodes
 */
public class MenuInitialiser {

    private final ComboBox<String> serialPortPicker = new ComboBox<>();
    private final ComboBox<Integer> patchPicker = new ComboBox<>();
    private final ComboBox<String> midiChZeroPicker = new ComboBox<>();
    private final ComboBox<String> midiChOnePicker = new ComboBox<>();

    private final Stage fileStage = new Stage();

    private final SerialCommandHandler serialCommandHandler;
    private final SerialPort serialPort;
    private final String[] serialPortNameList;

    final static ArrayList<IntField> paramFields = new ArrayList<>();

    final static AlertHandler alertHandler = new AlertHandler();

    final static MenuEventHandler menuEventHandler = MenuEventHandler.getSingleInstance();

    int BUTTON_WIDTH = 150;

    String style = this.getClass().getResource("/stylesheets/style.css").toExternalForm();



    public MenuInitialiser(SerialCommandHandler serialCommandHandler, SerialPort serialPort, String[] serialPortNameList) {
        this.serialCommandHandler = serialCommandHandler;
        this.serialPort = serialPort;
        this.serialPortNameList = serialPortNameList;
    }

    /**
     * Initialises and returns the main BorderPane used for the display
     * @return BorderPane
     * @throws IOException
     * @throws SerialPortException
     */
    public Scene initializeScene() throws IOException, SerialPortException {

        BorderPane topBorder = new BorderPane();
        BorderPane border = new BorderPane();
        TabPane tabPane = new TabPane();

        tabPane.setTabMinWidth(80);
        tabPane.setTabMaxWidth(80);
        tabPane.getTabs().addAll(initTabs());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        border.setCenter(tabPane);
        initPortSelection(serialPort);
        border.setLeft(getMenuBar());

        topBorder.setCenter(border);
        topBorder.setMaxWidth(1100);
        topBorder.setMinWidth(1100);
        topBorder.setMaxHeight(750);
        topBorder.setMinHeight(750);

        BorderPane.setAlignment(tabPane, Pos.CENTER);

        Scene scene = new Scene(topBorder);
        scene.getStylesheets().add(style);

        menuEventHandler.setParams(serialCommandHandler, paramFields, scene);

        midiChZeroPicker.getSelectionModel().selectFirst();
        int val = midiChZeroPicker.getSelectionModel().getSelectedIndex();
        menuEventHandler.onMidiChannelChange(UNIT_NUMBER.ZERO, val);

        midiChOnePicker.getSelectionModel().selectFirst();
        int val2 = midiChOnePicker.getSelectionModel().getSelectedIndex();
        menuEventHandler.onMidiChannelChange(UNIT_NUMBER.ONE, val2);

        menuEventHandler.setUnit(UNIT_NUMBER.ZERO);

        try {
            menuEventHandler.onPatchPicked(1);
            menuEventHandler.setLayering(false);
        } catch (SerialPortException | IOException e) {
            e.printStackTrace();
        }

        return scene;

    }

    /**
     * Initialises the port picker that allows user to change the active serial port
     * NOTE: The program will attempt to automatically choose an XFM2 device as the default active port
     * @param serialPort The serialport being worked with that eventually becomes the selected port
     * @throws IOException
     * @throws SerialPortException
     */
    public void initPortSelection(SerialPort serialPort) throws IOException, SerialPortException {
        if (serialPortNameList.length > 0) {
            for (String s : serialPortNameList) {
                SerialPort sP = new SerialPort(s);
                serialCommandHandler.setSerialPort(sP);
                System.out.println("Port Name: " + s);
                byte[] tempData = serialCommandHandler.getAllValues();
                System.out.println("DATA LENGTH " + tempData.length);
                if (tempData.length == 512) {
                    serialPort = sP;
                }
            }

            serialCommandHandler.setSerialPort(serialPort);
            for (String s : serialPortNameList) {
                serialPortPicker.getItems().add(s);
            }

            if(serialPort == null){
                serialPortPicker.getSelectionModel().selectFirst();
                serialPort = new SerialPort(serialPortPicker.getValue());
                serialCommandHandler.setSerialPort(serialPort);
            }

            serialPortPicker.getSelectionModel().clearSelection();
            serialPortPicker.getSelectionModel().select(serialPort.getPortName());
        } else {
            System.out.println("SETTING NULL ANYWAY FOR SOME REASON");
            serialPort = null;
            serialPortPicker.setPromptText("NO PORTS");
        }

        updateSerialPickerListener();
    }

    /**
     * Creates and initialises each tab through the TabInit class
     * @return ArrayList of tabs that are all ready for use
     */
    public ArrayList<Tab> initTabs() {
        TabInit tabInit = new TabInit();
        return tabInit.getTabs(paramFields);
    }

    /**
     * Creates the menu bar for the program, calling various methods to create buttons, comboboxes etc.
     * Sets all action events
     * @return The menu for the left part of the main BorderPane
     * TODO: Seperate into smaller sub-methods.
     */
    public VBox getMenuBar() {
        VBox xfmButtons = new VBox();
        VBox localButtons = new VBox();
        VBox serialPortSelection;

        Label xfmPatch = new Label("Program #:");

        Button[] menuButtons = getMenuButtons();

        ArrayList<Integer> vals = new ArrayList<>();
        for (int i = 1; i <= 127; i++) {
            vals.add(i);
        }

        patchPicker.getItems().addAll(vals);
        patchPicker.setOnAction(e -> {
            try {
                if (!(patchPicker.getValue() == null)) {
                    menuEventHandler.onPatchPicked(patchPicker.getValue());
                }
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }
        });
        patchPicker.getSelectionModel().selectFirst();

        //Button reloadTabs = new Button("Refresh Tabs");
        //reloadTabs.setTooltip(reloadTooltip);


        CheckBox liveChanges = new CheckBox("Live Changes");
        liveChanges.setSelected(true);
        EventHandler<ActionEvent> actionEventEventHandler = actionEvent -> menuEventHandler.onLiveChanged(liveChanges.isSelected());
        liveChanges.setOnAction(actionEventEventHandler);

        // EventHandler<? super MouseEvent> reloadTabsEventHandler = (EventHandler<MouseEvent>) mouseEvent -> reloadTabs();
        // reloadTabs.setOnMouseClicked(reloadTabsEventHandler);

        // Sets titles of each button sub-section
        Label serialPortLabel = new Label("Serial Port:");
        serialPortLabel.getStyleClass().add("button-group-title");

        Image logo = new Image(String.valueOf(getClass().getResource("/images/logo.png")));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(100);
        logoView.setFitWidth(100);

        Label xfmButtonsLabel = new Label("Device Controls");

        xfmButtonsLabel.getStyleClass().add("button-group-title");

        Label localButtonsLabel = new Label("Local Controls");
        localButtonsLabel.getStyleClass().add("button-group-title");

        Label subtitle = new Label("By Lewis Sweeney");

        // Sets preferred width of the serialPort Picker
        // TODO: Change this to CSS.
        serialPortPicker.setPrefWidth(BUTTON_WIDTH);

        // Creates new sub-sub-section for the patch control
        VBox patchControl = new VBox(xfmPatch, patchPicker);

        // Creates each subsection being added to menuLayout
        serialPortSelection = new VBox(serialPortLabel, serialPortPicker);
        xfmButtons.getChildren().addAll(xfmButtonsLabel, patchControl, menuButtons[0], menuButtons[1], menuButtons[2], getUnitControls());
        localButtons.getChildren().addAll(localButtonsLabel, menuButtons[3], menuButtons[4], liveChanges);

        // Assigns style classes for each required node.
        serialPortSelection.getStyleClass().add("button-row");
        patchControl.getStyleClass().add("button-row");
        xfmButtons.getStyleClass().add("button-row");
        localButtons.getStyleClass().add("button-row");
        subtitle.getStyleClass().add("app-subtitle");

        Hyperlink about = new Hyperlink("About");
        about.setOnAction(actionEvent -> {
            AboutSceneConstructor asc = new AboutSceneConstructor();
            asc.showStage();
        });

        VBox menuLayout = new VBox(logoView, about, xfmButtons, localButtons, serialPortSelection);

        menuLayout.getStyleClass().add("button-column");

        return menuLayout;
    }


    /**
     * Constructs the Unit subsection of the left-hand menu
     * @return VBox containing the required nodes
     */
    private VBox getUnitControls() {

        Label midiTitle = new Label("Unit and MIDI Controls:");
        ToggleGroup unitGroup = new ToggleGroup();

        VBox midiConOne = getIndividualMidiControl(midiChZeroPicker,UNIT_NUMBER.ZERO,unitGroup);
        midiConOne.getStyleClass().add("picker-set");

        VBox midiConTwo = getIndividualMidiControl(midiChOnePicker, UNIT_NUMBER.ONE,unitGroup);
        midiConTwo.getStyleClass().add("picker-set");

        HBox midiCons = new HBox(midiConOne, midiConTwo);

        CheckBox layering = new CheckBox("Layering");
        Tooltip layeringTooltip = new Tooltip("Toggle layering on the XFM2");
        layering.setTooltip(layeringTooltip);
        layering.setOnAction(e -> {
            try {
                menuEventHandler.setLayering(layering.isSelected());
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }
        });

        VBox layout = new VBox(midiTitle, midiCons, layering);
        layout.getStyleClass().add("unit-row");


        return layout;
    }

    /**
     * Creates an individual unit control, containing a combobox, label, and radio button
     * Initialises the action events for each necessary node
     * @return a VBox containing the required nodes
     */
    private VBox getIndividualMidiControl(ComboBox<String> midiPicker, UNIT_NUMBER unit_number, ToggleGroup toggleGroup) {
        for (int i = 0; i < 17; i++) {
            if (i == 0) {
                midiPicker.getItems().add("All");
            } else {
                midiPicker.getItems().add(String.valueOf(i));
            }
        }

        midiPicker.getStyleClass().add("channel-combo");

        midiPicker.setOnAction(e -> {
            try {
                int val = midiPicker.getSelectionModel().getSelectedIndex();
                menuEventHandler.onMidiChannelChange(unit_number, val);
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }

        });

        RadioButton unit = new RadioButton();

        unit.setOnAction(actionEvent -> {
            try {
                if (unit.isSelected()) {
                    menuEventHandler.setUnit(unit_number);
                }
            } catch (SerialPortException | IOException e) {
                e.printStackTrace();
            }
        });

        unit.setToggleGroup(toggleGroup);

        Label controlLabel = new Label("Unit 1");
        Tooltip midiTooltip = new Tooltip("Set the MIDI channel for Unit 1 on the device");

        if(unit_number == UNIT_NUMBER.ZERO){
            unit.setSelected(true);
            controlLabel.setText("Unit 0");
            midiTooltip.setText("Set the MIDI channel for Unit 0 on the device");
        }

        controlLabel.setTooltip(midiTooltip);
        midiPicker.getSelectionModel().selectFirst();

        return new VBox(controlLabel,unit,midiPicker);
    }

    /**
     * Creates all buttons required for the menu, assigning tooltips and action event handlers for each
     * @return Button[] containing each required button
     */
    private Button[] getMenuButtons() {

        Button[] menuButtons = new Button[5];

        menuButtons[0] = new Button("Read Device");
        Tooltip readTooltip = new Tooltip("Reads the current state of the XFM device");
        menuButtons[0].setTooltip(readTooltip);

        menuButtons[1] = new Button("Write to Device");
        Tooltip writeTooltip = new Tooltip("Writes the current parameters to the XFM");
        menuButtons[1].setTooltip(writeTooltip);

        menuButtons[2] = new Button("Save to Device");
        Tooltip saveXFMTooltip = new Tooltip("Saves the current parameters to the currently selected XFM patch");
        menuButtons[2].setTooltip(saveXFMTooltip);

        menuButtons[3] = new Button("Save Locally");
        Tooltip saveCurrentTooltip = new Tooltip("Save the current paramater set locally");
        menuButtons[3].setTooltip(saveCurrentTooltip);

        menuButtons[4] = new Button("Load Local Patch");
        Tooltip loadTooltip = new Tooltip("Load an XFM2 file and set the parameters");
        menuButtons[4].setTooltip(loadTooltip);

        menuButtons[0].setOnAction(actionEvent -> {
            try {
                menuEventHandler.onReadButtonPress();
            } catch (SerialPortException | IOException e) {
                e.printStackTrace();
            }
        });

        menuButtons[1].setOnAction(actionEvent -> {
            try {
                menuEventHandler.onWriteButtonPress();
            } catch (SerialPortException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        menuButtons[2].setOnAction(actionEvent -> {
            try {
                menuEventHandler.onSaveToXFMPress(patchPicker);
            } catch (IOException | SerialPortException e) {
                e.printStackTrace();
            }
        });


        menuButtons[3].setOnAction(actionEvent -> {
            try {
                menuEventHandler.onSaveButtonPress(fileStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuButtons[4].setOnAction(actionEvent -> {
            try {
                menuEventHandler.onLoadButtonPress(fileStage);
            } catch (SerialPortException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });

        return menuButtons;
    }

    /**
     * Updates the listener of the serialportpicker
     * Currently only used once, may move back to relevant method
     */
    public void updateSerialPickerListener() {
        serialPortPicker.setOnAction(e -> {
            try {
                menuEventHandler.onSerialPortSelection(serialPortNameList, serialPortPicker, serialPort, patchPicker);
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }
        });
    }
}
