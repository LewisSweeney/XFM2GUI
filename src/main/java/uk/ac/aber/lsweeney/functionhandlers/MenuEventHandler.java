package uk.ac.aber.lsweeney.functionhandlers;

import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortException;
import uk.ac.aber.lsweeney.enums.UNIT_NUMBER;
import uk.ac.aber.lsweeney.externalcode.IntField;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Handles all menu action events, performing commands and/or GUI changes
 */
public class MenuEventHandler {

    private static final MenuEventHandler SINGLE_INSTANCE = new MenuEventHandler();

    private final OptionsHandler optionsHandler = OptionsHandler.getSingleInstance();

    private SerialHandlerBridge serialHandler;
    private ArrayList<IntField> paramFields;

    FileLoader loader = new FileLoader();
    PatchSaver saver = new PatchSaver();

    public static MenuEventHandler getSingleInstance(){
        return SINGLE_INSTANCE;
    }


    public void setParams(SerialHandlerBridge serialHandler, ArrayList<IntField> paramFields){
        this.serialHandler = serialHandler;
        this.paramFields = paramFields;
    }

    /*
      UNUSED
      In the case of any tabs being closed accidentally, this will refresh the tabslist and return all tabs to the correct position
      This will also close any extra windows that the program has opened.
      TODO: Fix refresh bug where tabs draw over each other
     */
    /* private void reloadTabs() {
        List<Stage> stages = Window.getWindows().stream().filter(Stage.class::isInstance).map(Stage.class::cast).collect(Collectors.toList());
        for (Stage s : stages) {
            if (!s.getScene().equals(this.scene)) {
                s.close();
            } else {
                s.toFront();
            }
        }

        border.setCenter(null);
        tabPane.getTabs().clear();
        for(Tab t:tabs){
            tabPane.getTabs().add(t);
        }
        tabPane.getSelectionModel().clearAndSelect(0);
        tabPane.requestFocus();
        border.setCenter(tabPane);


    }*/

    /**
     * When the value of serialPortPicker changes, this method will change the active port to the one selected
     *
     * @throws SerialPortException Serial port may not be found
     */
    public void onSerialPortSelection(String[] serialPortNameList, ComboBox<String> serialPortPicker, SerialPort serialPort, ComboBox<Integer> patchPicker) throws SerialPortException, IOException {
        if (serialPort != null && serialPort.isOpened()) {
            serialPort.closePort();
        }
        if (serialPortNameList.length > 0) {
            String portName = serialPortPicker.getValue();
            serialPort = new SerialPort(portName);
            serialHandler.setSerialPort(serialPort);
            setAllIntFieldValues(serialHandler.getAllValues());
            patchPicker.getSelectionModel().clearSelection();
        }
    }

    /**
     * When the value of serialPortPicker changes, this method will change the active port to the one selected
     * VERSION FOR THE JSERIALCOMM LIBRARY
     */
    public void onSerialPortSelection(com.fazecast.jSerialComm.SerialPort[] serialPorts, ComboBox<String> serialPortPicker, com.fazecast.jSerialComm.SerialPort serialPort, ComboBox<Integer> patchPicker) throws SerialPortException, IOException {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }
        if (serialPorts.length > 0) {
            int portIndex = serialPortPicker.getSelectionModel().getSelectedIndex();
            serialPort = serialPorts[portIndex];
            serialHandler.setSerialPort(serialPort);
            setAllIntFieldValues(serialHandler.getAllValues());
            patchPicker.getSelectionModel().clearSelection();
        }
    }

    /**
     * Writes all current parameters to the board
     *
     * @throws SerialPortException Serial port may not be found
     * @throws IOException Data may not be read from device or written to BAOS
     */
    public void onWriteButtonPress() throws SerialPortException, IOException {
        paramFields.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));

        byte[] bytes = new byte[512];

        for(IntField p:paramFields){
            bytes[Integer.parseInt(p.getId())] = (byte) p.getValue();
        }

        serialHandler.setAllValues(bytes);

        //byte[] oldBytes = serialCommandHandler.getAllValues();
       /* if (oldBytes.length == 512) {
            for (IntField intField : paramFields) {
                int paramNum = Integer.parseInt(intField.getId());
                if (oldBytes[paramNum] != intField.getValue()) {
                    serialCommandHandler.setIndividualValue(Integer.parseInt(intField.getId()), intField.getValue());
                }
            }
        }
        */
    }

    /**
     * Handler for load button. Prompts user to load an XFM2 file to be loaded into the program
     *
     * @throws FileNotFoundException File may not exist - should be fine if resources not meddled with...
     */
    public void onLoadButtonPress(Stage fileStage) throws IOException, SerialPortException {
        paramFields.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        ArrayList<String> lines = loader.loadXFM2FromFile(fileStage);

        optionsHandler.setLiveChanges(false);

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

        optionsHandler.setLiveChanges(true);

        onWriteButtonPress();
    }

    /**
     * Handler for save button. Prompts user to specify save location and file name.
     *
     * @throws IOException Never thrown but IntelliJ not happy with removing it
     */
    @SuppressWarnings("RedundantThrows")
    public void onSaveButtonPress(Stage fileStage) throws IOException {
        paramFields.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        ArrayList<String> lines = new ArrayList<>();
        for (IntField i : paramFields) {
            lines.add(i.getId() + ":" + i.getValue());
        }
        saver.saveToFile(lines, fileStage);
    }

    /**
     * Reads the current values from the XFM2.
     * Only particularly useful if live updates are not enabled, as otherwise values
     * are likely to be the same.
     *
     * @throws SerialPortException Serial port may not be found
     * @throws IOException Data may not be read from device or written to BAOS
     */
    public void onReadButtonPress() throws SerialPortException, IOException {
        byte[] dump = serialHandler.getAllValues();
        setAllIntFieldValues(dump);
    }

    /**
     * Saves the current preset to the selected XFM2 patch number.
     * TODO: Fix bug with 0th patch not doing anything...
     *
     * @throws SerialPortException Serial port may not be found
     * @throws IOException Data may not be read from device or written to BAOS
     */
    public void onSaveToXFMPress(ComboBox<Integer> patchPicker) throws IOException, SerialPortException {
        onWriteButtonPress();
        serialHandler.writeProgram(patchPicker.getValue());
    }

    /**
     * Activates when user changes patch from the drop down menu
     *
     * @param value The value of the patchpicker when chosen - used to determine which patch is loaded
     *
     * @throws SerialPortException Serial port may not be found
     * @throws IOException Data may not be read from device or written to BAOS
     */
    public void onPatchPicked(int value) throws SerialPortException, IOException {
        optionsHandler.setLiveChanges(false);
        serialHandler.readProgram(value);
        setAllIntFieldValues(serialHandler.getAllValues());
        optionsHandler.revertLiveChanges();
    }

    /**
     * Changes the midi channel for the relevant unit_number to the one selected by the user
     * @param unit_number Enum, either ZERO or ONE
     * @param channel Midi Channel chosen from combobox
     *
     * @throws SerialPortException Serial port may not be found
     * @throws IOException Data may not be read from device or written to BAOS
     */
    public void onMidiChannelChange(UNIT_NUMBER unit_number, int channel) throws IOException, SerialPortException {
        serialHandler.setMidiChannel(unit_number, channel);
    }

    /**
     * Sets all of the parameter field values to its corresponding position in the dump array
     * Called when changing patch/program
     *
     * @param dump The 512 length byte array containing all parameters.
     */
    public void setAllIntFieldValues(byte[] dump) {
        // int offset = 48;
        if (dump.length == 512) {
            for (IntField intField : paramFields) {
                // int paramAddress = Integer.parseInt(intField.getId()) + offset;
                intField.setValue(dump[Integer.parseInt(intField.getId())] & 0xff);
            }
        }
    }

    /**
     * Changes the active unit to the one selected from the unit radio buttons
     * @param unit_number Enum that determines whether unit ZERO or ONE is active
     *
     * @throws SerialPortException Serial port may not be found
     * @throws IOException Data may not be read from device or written to BAOS
     */
    public void setUnit(UNIT_NUMBER unit_number) throws IOException, SerialPortException {
        serialHandler.setUnit(unit_number);
    }

    public void setLayering(boolean layering) throws IOException, SerialPortException {
        serialHandler.setMidiLayering(layering);
    }

    /**
     * Handles the changing of the state of the liveChanges checkbox
     * @param live Boolean to determine whether live changes is on or off
     */
    public void onLiveChanged(boolean live){
        optionsHandler.setLiveChanges(live);
    }
}
