package main.java;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.stage.Window;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import main.java.externalcode.DraggableTab;
import main.java.externalcode.IntField;
import main.java.serial.MIDI_CHANNEL;
import main.java.serial.SerialCommandHandler;
import main.java.serial.UNIT_NUMBER;
import main.java.tabconstructors.*;
import main.java.utilities.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends Application {

    private static final int BOTTOM_BUTTON_WIDTH = 150;

    private final ArrayList<Tab> allTabs = new ArrayList<>();
    private ArrayList<Tab> tabs;

    private BorderPane topBorder;
    private BorderPane border;
    private TabPane tabPane;
    private Scene scene;

    private final ComboBox<String> serialPortPicker = new ComboBox<>();
    private final ComboBox<Integer> patchPicker = new ComboBox<>();
    private final ComboBox<String> midiChOnePicker = new ComboBox<>();
    private final ComboBox<String> midiChTwoPicker = new ComboBox<>();

    boolean layering = false;

    private final Stage fileStage = new Stage();
    String[] serialPortNameList = SerialPortList.getPortNames();
    SerialPort serialPort;
    SerialCommandHandler serialCommandHandler = new SerialCommandHandler(serialPort);

    Boolean LIVE_CHANGES = false;

    final static ArrayList<IntField> paramFields = new ArrayList<>();

    String style = this.getClass().getResource("/stylesheets/style.css").toExternalForm();

    @Override
    public void start(Stage primaryStage) throws IOException, SerialPortException {

        initializeScene();
        ParamValueChange.setSerialCommandHandler(serialCommandHandler);

        LIVE_CHANGES = false;
        setAllIntFieldValues(serialCommandHandler.getAllValues());
        LIVE_CHANGES = true;

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("XFM2 GUI");
        primaryStage.show();
        serialCommandHandler.setLIVE_CHANGES(true);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Initialises the BorderPane used as the main node for the GUI
    private void initializeScene() throws IOException, SerialPortException {


        topBorder = new BorderPane();
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

    }

    /* Initialises the buttons at the bottom of the page
     */
    private void initButtons() {
        VBox xfmButtons = new VBox();
        VBox localButtons = new VBox();
        VBox serialPortSelection = new VBox();

        Label xfmPatch = new Label("XFM2 Program #:");

        EventHandler<? super MouseEvent> saveXFMEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                onSaveToXFMPress();
            } catch (SerialPortException | IOException e) {
                e.printStackTrace();
            }
        };
        Button saveToXFM = new Button("Save To XFM2");
        saveToXFM.setOnMouseClicked(saveXFMEventHandler);

        ArrayList<Integer> vals = new ArrayList<>();
        for (int i = 0; i <= 127; i++) {
            vals.add(i);
        }
        patchPicker.getItems().addAll(vals);

        patchPicker.setOnAction(e -> {
            try {
                onPatchPicked(patchPicker.getValue());
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }
        });
        patchPicker.getSelectionModel().selectFirst();

        Button read = new Button("Read XFM2");
        Button write = new Button("Write to XFM2");
        Button setUnit0 = new Button("Set Unit 0");
        Button setUnit1 = new Button("Set Unit 1");
        Button saveCurrentPatch = new Button("Save Program");
        Button loadPatch = new Button("Load Program");
        Button reloadTabs = new Button("Refresh Tabs");

        EventHandler<? super MouseEvent> readEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                onReadButtonPress();
            } catch (SerialPortException | IOException e) {
                e.printStackTrace();
            }

        };
        read.setOnMouseClicked(readEventHandler);

        EventHandler<? super MouseEvent> saveEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                onSaveButtonPress();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        saveCurrentPatch.setOnMouseClicked(saveEventHandler);

        EventHandler<? super MouseEvent> loadEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                onLoadButtonPress();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        };
        loadPatch.setOnMouseClicked(loadEventHandler);

        EventHandler<? super MouseEvent> writeEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                onWriteButtonPress();
            } catch (SerialPortException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        write.setOnMouseClicked(writeEventHandler);

        EventHandler<? super MouseEvent> reloadTabsEventHandler = (EventHandler<MouseEvent>) mouseEvent -> reloadTabs();
        reloadTabs.setOnMouseClicked(reloadTabsEventHandler);

        // Sets titles of each button sub-section
        Label serialPortLabel = new Label("Serial Port:");
        serialPortLabel.getStyleClass().add("button-group-title");

        Label xfmButtonsLabel = new Label("XFM2 Controls");
        xfmButtonsLabel.getStyleClass().add("button-group-title");

        Label localButtonsLabel = new Label("Local Controls");
        localButtonsLabel.getStyleClass().add("button-group-title");

        Label title = new Label("XFM2-GUI");
        Label subtitle = new Label("By Lewis Sweeney");

        // Sets preferred width of the serialPort Picker
        // TODO: Change this to CSS.
        serialPortPicker.setPrefWidth(BOTTOM_BUTTON_WIDTH);

        // Creates new sub-sub-section for the patch control
        VBox patchControl = new VBox(xfmPatch, patchPicker, saveToXFM);

        // Creates each subsection being added to buttonSet
        serialPortSelection = new VBox(serialPortLabel, serialPortPicker);
        xfmButtons.getChildren().addAll(xfmButtonsLabel, read, write, patchControl, initMidiControl());
        localButtons.getChildren().addAll(localButtonsLabel, saveCurrentPatch, loadPatch, reloadTabs);

        // Assigns style classes for each required node.
        serialPortSelection.getStyleClass().add("button-row");
        patchControl.getStyleClass().add("button-row");
        xfmButtons.getStyleClass().add("button-row");
        localButtons.getStyleClass().add("button-row");
        title.getStyleClass().add("app-title");
        subtitle.getStyleClass().add("app-subtitle");

        VBox buttonSet = new VBox(title, subtitle, xfmButtons, localButtons, serialPortSelection);
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
                onMidiChannelChange(UNIT_NUMBER.ONE,val);
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }

        });

        midiChTwoPicker.setOnAction(e -> {
            try {
                int val = midiChOnePicker.getSelectionModel().getSelectedIndex();
                onMidiChannelChange(UNIT_NUMBER.TWO,val);
            } catch (SerialPortException | IOException serialPortException) {
                serialPortException.printStackTrace();
            }

        });


        Label midiTitle = new Label("MIDI Controls:");

        Label midiOne = new Label("Unit 1");
        VBox midiConOne = new VBox(midiOne, midiChOnePicker);
        midiConOne.getStyleClass().add("button-row");

        Label midiTwo = new Label("Unit 2");
        VBox midiConTwo = new VBox(midiTwo, midiChTwoPicker);
        midiConTwo.getStyleClass().add("button-row");

        HBox midiCons = new HBox(midiConOne, midiConTwo);

        CheckBox layering = new CheckBox("Layering");
        layering.setOnAction(e ->{
            try{
                serialCommandHandler.setMidiLayering(layering.isSelected());
            } catch (SerialPortException serialPortException) {
                serialPortException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        VBox layout = new VBox(midiTitle, midiCons, layering);
        layout.getStyleClass().add("button-row");


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
            serialPortPicker.getItems().add("-NO PORTS AVAILABLE-");
        }

        serialPortPicker.setOnAction(e -> {
            try {
                onSerialPortSelection();
            } catch (SerialPortException serialPortException) {
                serialPortException.printStackTrace();
            }

        });
    }

    // Initialises the previously created tabs with the relevant nodes/content
    private void initTabs() {

        tabs = new ArrayList<>();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/parameters/tablist.txt")));
        try {
            String line = bReader.readLine();
            while (line != null) {
                DraggableTab t = new DraggableTab(line);
                t.setClosable(false);
                tabs.add(t);
                line = bReader.readLine();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        allTabs.addAll(tabs);


        ArrayList<String> op1FilePaths = new ArrayList<>();
        op1FilePaths.add("/parameters/operators/op1.txt");

        ArrayList<String> op2FilePaths = new ArrayList<>();
        op2FilePaths.add("/parameters/operators/op2.txt");

        ArrayList<String> op3FilePaths = new ArrayList<>();
        op3FilePaths.add("/parameters/operators/op3.txt");

        ArrayList<String> op4FilePaths = new ArrayList<>();
        op4FilePaths.add("/parameters/operators/op4.txt");

        ArrayList<String> op5FilePaths = new ArrayList<>();
        op5FilePaths.add("/parameters/operators/op5.txt");

        ArrayList<String> op6FilePaths = new ArrayList<>();
        op6FilePaths.add("/parameters/operators/op6.txt");

        ArrayList<String> progFilePaths = new ArrayList<>();
        progFilePaths.add("/parameters/program/lfo.txt");
        progFilePaths.add("/parameters/program/pitcheg.txt");
        progFilePaths.add("/parameters/program/other.txt");

        ArrayList<String> effectsFilePaths = new ArrayList<>();
        effectsFilePaths.add("/parameters/effects/am.txt");
        effectsFilePaths.add("/parameters/effects/bitcrusher.txt");
        effectsFilePaths.add("/parameters/effects/chorusflanger.txt");
        effectsFilePaths.add("/parameters/effects/decimator.txt");
        effectsFilePaths.add("/parameters/effects/delay.txt");
        effectsFilePaths.add("/parameters/effects/fxrouting.txt");
        effectsFilePaths.add("/parameters/effects/phaser.txt");
        effectsFilePaths.add("/parameters/effects/reverb.txt");
        effectsFilePaths.add("/parameters/effects/filter.txt");

        ArrayList<String> modulationFilePaths = new ArrayList<>();
        modulationFilePaths.add("/parameters/modulation/amplfo.txt");
        modulationFilePaths.add("/parameters/modulation/arpeggiator.txt");
        modulationFilePaths.add("/parameters/modulation/egbias.txt");
        modulationFilePaths.add("/parameters/modulation/perfcontrols.txt");
        modulationFilePaths.add("/parameters/modulation/pitch.txt");
        modulationFilePaths.add("/parameters/modulation/pitchlfo.txt");

        TabConstructor tabConstructor = new TabConstructor();

        tabs.get(0).setContent(tabConstructor.getLayout(op1FilePaths, getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op1));
        tabs.get(1).setContent(tabConstructor.getLayout(op2FilePaths, getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op2));
        tabs.get(2).setContent(tabConstructor.getLayout(op3FilePaths, getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op3));
        tabs.get(3).setContent(tabConstructor.getLayout(op4FilePaths, getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op4));
        tabs.get(4).setContent(tabConstructor.getLayout(op5FilePaths, getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op5));
        tabs.get(5).setContent(tabConstructor.getLayout(op6FilePaths, getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op6));
        tabs.get(6).setContent(tabConstructor.getLayout(progFilePaths, getTabGroupValues(REQUIRED_TAB.prog), OPERATOR_NUM.no));
        tabs.get(7).setContent(tabConstructor.getLayout(modulationFilePaths, getTabGroupValues(REQUIRED_TAB.mod), OPERATOR_NUM.no));
        tabs.get(8).setContent(tabConstructor.getLayout(effectsFilePaths, getTabGroupValues(REQUIRED_TAB.fx), OPERATOR_NUM.no));

        paramFields.addAll(tabConstructor.getIntFields());
    }

    /**
     * Gets the required group values, which are how the various components of each tab are grouped.
     *
     * @param r Required tab type, Enum
     * @return Returns the values of the required groups as an arraylist of strings that contain groupings.
     */
    public ArrayList<String> getTabGroupValues(REQUIRED_TAB r) {

        String filepath = switch (r) {
            case op -> "/parameters/groupValues/operator.txt";
            case fx -> "/parameters/groupValues/effects.txt";
            case mod -> "/parameters/groupValues/modulation.txt";
            case prog -> "/parameters/groupValues/program.txt";
        };

        BufferedReader bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filepath)));
        ArrayList<String> programGroupValues = new ArrayList<>();
        try {
            String line = bReader.readLine();
            while (line != null) {
                programGroupValues.add(line);
                line = bReader.readLine();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return programGroupValues;
    }

    /**
     * In the case of any tabs being closed accidentally, this will refresh the tabslist and return all tabs to the correct position
     * This will also close any extra windows that the program has opened.
     * TODO: Fix refresh bug where tabs draw over each other
     */
    private void reloadTabs() {
        tabPane.getTabs().clear();
        tabs.clear();
        tabs.addAll(allTabs);
        tabPane.getTabs().addAll(tabs);
        System.out.println(tabPane.getSelectionModel().getSelectedIndex());
        tabPane.getSelectionModel().clearAndSelect(0);
        tabPane.getSelectionModel().selectFirst();

        List<Stage> stages = Window.getWindows().stream().filter(Stage.class::isInstance).map(Stage.class::cast).collect(Collectors.toList());
        for (Stage s : stages) {
            if (!s.getScene().equals(this.scene)) {
                s.close();
            } else {
                s.toFront();
            }
        }
    }

    /**
     * When the value of serialPortPicker changes, this method will change the active port to the one selected
     *
     * @throws SerialPortException
     */
    public void onSerialPortSelection() throws SerialPortException {
        if (serialPort.isOpened()) {
            serialPort.closePort();
        }
        if (serialPortNameList.length > 0) {
            String portName = serialPortPicker.getValue();
            serialPort = new SerialPort(portName);
            serialCommandHandler.setSerialPort(serialPort);
        }
    }

    /**
     * Writes all current param
     *
     * @throws SerialPortException
     * @throws IOException
     * @throws InterruptedException
     */
    private void onWriteButtonPress() throws SerialPortException, IOException, InterruptedException {
        paramFields.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));

        byte[] oldBytes = serialCommandHandler.getAllValues();
        if (oldBytes.length == 512) {
            for (IntField intField : paramFields) {
                int paramNum = Integer.parseInt(intField.getId());
                if (oldBytes[paramNum] != intField.getValue()) {
                    serialCommandHandler.setIndividualValue(Integer.parseInt(intField.getId()), intField.getValue());
                }
            }
        }
    }

    /**
     * Handler for load button. Prompts user to load an XFM2 file to be loaded into the program
     *
     * @throws FileNotFoundException
     */
    private void onLoadButtonPress() throws FileNotFoundException {
        paramFields.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        PatchLoader loader = new PatchLoader();
        ArrayList<String> lines = loader.loadFromFile(fileStage);

        for (String line : lines) {
            String[] lineSplit = line.split(":");
            if (lineSplit.length == 2) {
                for (IntField i : paramFields) {
                    if (i.getId().equals(lineSplit[0])) {
                        i.setValue(Integer.parseInt(lineSplit[1]));
                    }
                }
            }
        }
    }

    /**
     * Handler for save button. Prompts user to specify save location and file name.
     *
     * @throws IOException
     */
    private void onSaveButtonPress() throws IOException {
        paramFields.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        ArrayList<String> lines = new ArrayList<>();
        for (IntField i : paramFields) {
            lines.add(i.getId() + ":" + i.getValue());
        }
        PatchSaver saver = new PatchSaver();
        saver.saveToFile(lines, fileStage);
    }

    /**
     * Reads the current values from the XFM2.
     * Only particularly useful if live updates are not enabled, as otherwise values
     * are likely to be the same.
     *
     * @throws SerialPortException
     * @throws IOException
     */
    private void onReadButtonPress() throws SerialPortException, IOException {
        byte[] dump = serialCommandHandler.getAllValues();
        setAllIntFieldValues(dump);
    }

    /**
     * Saves the current preset to the selected XFM2 patch number.
     * TODO: Fix bug with 0th patch not doing anything...
     *
     * @throws IOException
     * @throws SerialPortException
     */
    private void onSaveToXFMPress() throws IOException, SerialPortException {
        serialCommandHandler.writeProgram(patchPicker.getValue());
    }

    /**
     * Activates when user changes patch from the drop down menu
     *
     * @param value
     * @throws SerialPortException
     * @throws IOException
     */
    private void onPatchPicked(int value) throws SerialPortException, IOException {
        serialCommandHandler.setLIVE_CHANGES(false);
        System.out.println("Getting patch number " + value);
        serialCommandHandler.readProgram(value);
        setAllIntFieldValues(serialCommandHandler.getAllValues());
        serialCommandHandler.setLIVE_CHANGES(true);
    }

    private void onMidiChannelChange(UNIT_NUMBER unit, int channel) throws IOException, SerialPortException {
        serialCommandHandler.setMidiChannel(unit, channel);
    }

    /**
     * Sets all of the parameter field values to its corresponding position in the dump array
     * Called when changing patch/program
     *
     * @param dump The 512 length byte array containing all parameters.
     */
    private void setAllIntFieldValues(byte[] dump) {
        // int offset = 48;
        if (dump.length == 512) {
            for (IntField intField : paramFields) {
                // int paramAddress = Integer.parseInt(intField.getId()) + offset;
                intField.setValue(dump[Integer.parseInt(intField.getId())] & 0xff);
            }
        }
    }


}
