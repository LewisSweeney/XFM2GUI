package uk.ac.aber.lsweeney.initializers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import uk.ac.aber.lsweeney.enums.UNIT_NUMBER;
import uk.ac.aber.lsweeney.externalcode.IntField;
import uk.ac.aber.lsweeney.functionhandlers.MenuEventHandler;
import uk.ac.aber.lsweeney.functionhandlers.OptionsHandler;
import uk.ac.aber.lsweeney.sceneconstructors.AboutSceneConstructor;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

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

    BorderPane border = null;

    private final Stage fileStage = new Stage();

    private final SerialHandlerBridge serialHandler = SerialHandlerBridge.getSINGLE_INSTANCE();

    private String[] serialPortNameList;

    private SerialPort serialPortJSSC;

    private com.fazecast.jSerialComm.SerialPort serialPortJSerialComm;

    final static ArrayList<IntField> paramFields = new ArrayList<>();

    final static MenuEventHandler menuEventHandler = MenuEventHandler.getSingleInstance();

    int BUTTON_WIDTH = 150;

    static byte[] readData = null;

    int LOGO_DIM = 100;
    int TAB_WIDTH = 55;

    String style = this.getClass().getResource("/stylesheets/style.css").toExternalForm();

    /**
     * Initialises and returns the main BorderPane used for the display
     *
     * @return BorderPane
     *
     * @throws SerialPortException Serial port may not be found
     * @throws IOException Data may fail to be read from device or written to BAOS
     */
    public Scene initializeScene() throws IOException, SerialPortException {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        if(screenBounds.getWidth() < 1200 || screenBounds.getHeight()< 900){
            System.out.println(screenBounds);
            LOGO_DIM = LOGO_DIM / 2;
            TAB_WIDTH = TAB_WIDTH / 2;
            BUTTON_WIDTH = BUTTON_WIDTH / 2;
            style = this.getClass().getResource("/stylesheets/lowres/lowresstyle.css").toExternalForm();
        }
        BorderPane topBorder = new BorderPane();
        border = new BorderPane();
        TabPane tabPane = new TabPane();

        tabPane.setTabMinWidth(TAB_WIDTH);
        tabPane.setTabMaxWidth(TAB_WIDTH);
        tabPane.getTabs().addAll(initTabs());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        border.setCenter(tabPane);
        initPortSelection();
        border.setLeft(getMenuBar());

        topBorder.setCenter(border);
        topBorder.getStyleClass().add("window-size");

        BorderPane.setAlignment(tabPane, Pos.CENTER);

        Scene scene = new Scene(topBorder);
        scene.getStylesheets().add(style);

        menuEventHandler.setParams(serialHandler, paramFields);

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
     *
     * @throws SerialPortException Serial port may not be found
     * @throws IOException Data may fail to be read from device or written to BAOS
     */
    public void initPortSelection() throws IOException, SerialPortException {

        switch (serialHandler.getLibrary_choice()) {
            case JSSC -> {

                serialPortNameList = SerialPortList.getPortNames();

                for(String s:SerialPortList.getPortNames()){
                    System.out.println(s);
                }

                if (serialPortNameList.length > 0) {
                    for (String s : serialPortNameList) {
                        SerialPort sP = new SerialPort(s);
                        serialHandler.setSerialPort(sP);
                        byte[] tempData = serialHandler.getAllValues();
                        if (tempData.length == 512) {
                            serialPortJSSC = sP;
                            readData = tempData;
                        }
                    }

                    serialHandler.setSerialPort(serialPortJSSC);
                    for (String s : serialPortNameList) {
                        serialPortPicker.getItems().add(s);
                    }

                    if (serialPortJSSC == null) {
                        serialPortPicker.getSelectionModel().selectFirst();
                        serialPortJSSC = new SerialPort(serialPortPicker.getValue());
                        serialHandler.setSerialPort(serialPortJSSC);
                    }

                    serialPortPicker.getSelectionModel().clearSelection();
                    serialPortPicker.getSelectionModel().select(serialPortJSSC.getPortName());

                } else {
                    serialPortJSSC = null;
                    serialPortPicker.setPromptText("NO PORTS");
                }
            }

            case JSERIALCOMM -> {

                com.fazecast.jSerialComm.SerialPort serialPort = null;
                com.fazecast.jSerialComm.SerialPort[] serialPorts = com.fazecast.jSerialComm.SerialPort.getCommPorts();

                if (serialPorts.length > 0) {

                    for (com.fazecast.jSerialComm.SerialPort s : serialPorts) {
                        serialPortPicker.getItems().add(s.getSystemPortName());

                        serialHandler.setSerialPort(s);
                        byte[] tempData = serialHandler.getAllValues();
                        if (tempData.length == 512) {
                            serialPort = s;
                            readData = tempData;
                            break;
                        }
                    }

                    serialHandler.setSerialPort(serialPort);

                    if (serialPort == null) {
                        serialPortPicker.getSelectionModel().selectFirst();
                        serialPort = serialPorts[serialPortPicker.getSelectionModel().getSelectedIndex()];
                        serialHandler.setSerialPort(serialPort);
                    }

                    serialPortPicker.getSelectionModel().clearSelection();
                    serialPortPicker.getSelectionModel().select(serialPort.getSystemPortName());
                } else {
                    serialPortPicker.setPromptText("NO PORTS");
                }

                serialPortJSerialComm = serialPort;

            }
        }
        updateSerialPickerListener();
    }

    /**
     * Creates and initialises each tab through the TabInit class
     *
     * @return ArrayList of tabs that are all ready for use
     */
    public ArrayList<Tab> initTabs() {
        TabInit tabInit = new TabInit();
        return tabInit.getTabs(paramFields);
    }

    /**
     * Creates the menu bar for the program, calling various methods to create buttons, comboboxes etc.
     * Sets all action events
     *
     * @return The menu for the left part of the main BorderPane
     * TODO: Seperate into smaller sub-methods.
     */
    public VBox getMenuBar() {
        VBox xfmControls = new VBox();
        VBox localControls = new VBox();
        VBox serialPortSelection;

        Label xfmPatch = new Label("Program #:");
        xfmPatch.getStyleClass().add("menu-label");

        Button[] menuButtons = getMenuButtons();
        Button serialRefresh = new Button("Reload Port");

        switch (serialHandler.getLibrary_choice()){
            case JSSC -> serialRefresh.setOnAction(e -> {
                try {
                    menuEventHandler.onSerialPortSelection(serialPortNameList, serialPortPicker, serialPortJSSC, patchPicker);
                } catch (SerialPortException | IOException serialPortException) {
                    serialPortException.printStackTrace();
                }
            });

            case JSERIALCOMM -> serialRefresh.setOnAction(e -> {
                try {
                    menuEventHandler.onSerialPortSelection(com.fazecast.jSerialComm.SerialPort.getCommPorts(), serialPortPicker, serialPortJSerialComm, patchPicker);
                }
                catch (SerialPortException | IOException serialPortException) {
                    serialPortException.printStackTrace();
                }
            });
        }

        ArrayList<Integer> vals = new ArrayList<>();
        for (int i = 1; i <= 127; i++) {
            vals.add(i);
        }

        patchPicker.getStyleClass().add("channel-combo");
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

        //Button reloadTabs = new Button("Refresh Tabs");
        //reloadTabs.setTooltip(reloadTooltip);


        CheckBox liveChanges = new CheckBox("Live Changes");
        liveChanges.getStyleClass().add("menu-check-layout");
        EventHandler<ActionEvent> actionEventEventHandler = actionEvent -> menuEventHandler.onLiveChanged(liveChanges.isSelected());
        liveChanges.setOnAction(actionEventEventHandler);
        liveChanges.setSelected(false);
        OptionsHandler.getSingleInstance().setLiveChanges(false);

        // EventHandler<? super MouseEvent> reloadTabsEventHandler = (EventHandler<MouseEvent>) mouseEvent -> reloadTabs();
        // reloadTabs.setOnMouseClicked(reloadTabsEventHandler);

        // Sets titles of each button sub-section
        Label serialPortLabel = new Label("Serial Port:");
        serialPortLabel.getStyleClass().add("button-group-title");

        Image logo = new Image(String.valueOf(getClass().getResource("/images/logo.png")));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(LOGO_DIM);
        logoView.setFitWidth(LOGO_DIM);

        Label xfmButtonsLabel = new Label("Device Controls");

        xfmButtonsLabel.getStyleClass().add("button-group-title");

        Label localButtonsLabel = new Label("Local Controls");
        localButtonsLabel.getStyleClass().add("button-group-title");

        //Label subtitle = new Label("By Lewis Sweeney");

        // Sets preferred width of the serialPort Picker
        // TODO: Change this to CSS.
        serialPortPicker.setPrefWidth(BUTTON_WIDTH);

        serialRefresh.setMinWidth(100);

        // Creates new sub-sub-section for the patch control
        VBox patchControl = new VBox(xfmPatch, patchPicker);


        // Creates each subsection being added to menuLayout
        serialPortSelection = new VBox(serialPortLabel, serialPortPicker, serialRefresh);
        HBox xfmButtonBox = new HBox(menuButtons[0], menuButtons[1]);
        xfmControls.getChildren().addAll(xfmButtonsLabel, xfmButtonBox, patchControl, menuButtons[2], getUnitControls());
        HBox localButtonHBox = new HBox(menuButtons[3], menuButtons[4]);
        localControls.getChildren().addAll(localButtonsLabel, localButtonHBox, liveChanges);

        // Assigns style classes for each required node.
        serialPortSelection.getStyleClass().add("button-row");
        patchControl.getStyleClass().add("button-row");
        xfmControls.getStyleClass().add("button-row");
        localButtonHBox.getStyleClass().add("button-box");
        xfmButtonBox.getStyleClass().add("button-box");
        localControls.getStyleClass().add("button-row");

        Hyperlink about = new Hyperlink("About");
        about.setOnAction(actionEvent -> {
            AboutSceneConstructor asc = new AboutSceneConstructor();
            asc.showStage();
        });

        VBox menuLayout = new VBox(logoView, about, xfmControls, localControls, serialPortSelection);

        menuLayout.getStyleClass().add("button-column");

        return menuLayout;
    }


    /**
     * Constructs the Unit subsection of the left-hand menu
     *
     * @return VBox containing the required nodes
     */
    private VBox getUnitControls() {

        Label midiTitle = new Label("Unit and MIDI Controls:");
        midiTitle.getStyleClass().add("menu-label");
        ToggleGroup unitGroup = new ToggleGroup();

        GridPane midiConOne = getIndividualMidiControl(midiChZeroPicker, UNIT_NUMBER.ZERO, unitGroup);
        midiConOne.getStyleClass().add("picker-set");

        GridPane midiConTwo = getIndividualMidiControl(midiChOnePicker, UNIT_NUMBER.ONE, unitGroup);
        midiConTwo.getStyleClass().add("picker-set");

        VBox midiCons = new VBox(midiConOne, midiConTwo);

        CheckBox layering = new CheckBox("Layering");
        Tooltip layeringTooltip = new Tooltip("Toggle layering on the XFM2");
        layering.setTooltip(layeringTooltip);
        layering.getStyleClass().add("menu-check-layout");
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
     *
     * @return a VBox containing the required nodes
     */
    private GridPane getIndividualMidiControl(ComboBox<String> midiPicker, UNIT_NUMBER unit_number, ToggleGroup toggleGroup) {
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

        Label controlLabel = new Label("Unit 1:");
        controlLabel.getStyleClass().add("menu-label");
        Tooltip midiTooltip = new Tooltip("Set the MIDI channel for Unit 1 on the device");

        if (unit_number == UNIT_NUMBER.ZERO) {
            unit.setSelected(true);
            controlLabel.setText("Unit 0:");
            midiTooltip.setText("Set the MIDI channel for Unit 0 on the device");
        }

        controlLabel.setTooltip(midiTooltip);
        midiPicker.getSelectionModel().selectFirst();

        GridPane gridPane = new GridPane();
        gridPane.addRow(3, controlLabel, unit, midiPicker);

        return gridPane;
    }

    /**
     * Creates all buttons required for the menu, assigning tooltips and action event handlers for each
     *
     * @return Button[] containing each required button
     */
    private Button[] getMenuButtons() {

        Button[] menuButtons = new Button[5];

        menuButtons[0] = new Button("Read");
        Tooltip readTooltip = new Tooltip("Reads the current state of the XFM device");
        menuButtons[0].setTooltip(readTooltip);

        menuButtons[1] = new Button("Write");
        Tooltip writeTooltip = new Tooltip("Writes the current parameters to the XFM");
        menuButtons[1].setTooltip(writeTooltip);

        menuButtons[2] = new Button("Save");
        Tooltip saveXFMTooltip = new Tooltip("Saves the current parameters to the currently selected XFM patch");
        menuButtons[2].setTooltip(saveXFMTooltip);

        menuButtons[3] = new Button("Save");
        Tooltip saveCurrentTooltip = new Tooltip("Save the current paramater set locally");
        menuButtons[3].setTooltip(saveCurrentTooltip);

        menuButtons[4] = new Button("Load");
        Tooltip loadTooltip = new Tooltip("Open an XFM2 file and set the parameters within the program");
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
            } catch (SerialPortException | IOException e) {
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
            } catch (SerialPortException | IOException e) {
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
        switch (serialHandler.getLibrary_choice()){
            case JSSC -> serialPortPicker.setOnAction(e -> {
                try {
                    menuEventHandler.onSerialPortSelection(serialPortNameList, serialPortPicker, serialPortJSSC, patchPicker);
                } catch (SerialPortException | IOException serialPortException) {
                    serialPortException.printStackTrace();
                }
            });

            case JSERIALCOMM -> serialPortPicker.setOnAction(e -> {
                try {
                    menuEventHandler.onSerialPortSelection(com.fazecast.jSerialComm.SerialPort.getCommPorts(), serialPortPicker, serialPortJSerialComm, patchPicker);
                }
                catch (SerialPortException | IOException serialPortException) {
                    serialPortException.printStackTrace();
                }
            });
        }

    }

    public byte[] getReadData(){
        return readData;
    }
}
