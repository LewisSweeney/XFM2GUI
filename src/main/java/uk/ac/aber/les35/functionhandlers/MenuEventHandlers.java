package uk.ac.aber.les35.functionhandlers;

import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortException;
import uk.ac.aber.les35.externalcode.IntField;
import uk.ac.aber.les35.enums.UNIT_NUMBER;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class MenuEventHandlers {

    static SerialCommandHandler serialCommandHandler;
    static ArrayList<IntField> paramFields;
    static Scene scene;
   // static SerialPort serialPort;

    public static void setParams(SerialCommandHandler serialCommandHandler, ArrayList<IntField> paramFields, Scene scene){
        MenuEventHandlers.serialCommandHandler = serialCommandHandler;
        MenuEventHandlers.scene = scene;
        MenuEventHandlers.paramFields = paramFields;
    }


    /*
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
     * @throws SerialPortException
     */
    public static void onSerialPortSelection(String[] serialPortNameList, ComboBox<String> serialPortPicker, SerialPort serialPort, ComboBox<Integer> patchPicker) throws SerialPortException, IOException {
        if (serialPort != null && serialPort.isOpened()) {
            serialPort.closePort();
        }
        if (serialPortNameList.length > 0) {
            String portName = serialPortPicker.getValue();
            serialPort = new SerialPort(portName);
            serialCommandHandler.setSerialPort(serialPort);
            setAllIntFieldValues(serialCommandHandler.getAllValues());
            patchPicker.getSelectionModel().clearSelection();
        }
    }

    /**
     * Writes all current param
     *
     * @throws SerialPortException
     * @throws IOException
     * @throws InterruptedException
     */
    public static void onWriteButtonPress() throws SerialPortException, IOException, InterruptedException {
        paramFields.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));

        byte[] bytes = new byte[512];

        for(IntField p:paramFields){
            bytes[Integer.parseInt(p.getId())] = (byte) p.getValue();
        }

        serialCommandHandler.setAllValues(bytes);

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
     * @throws FileNotFoundException
     */
    public static void onLoadButtonPress(Stage fileStage) throws FileNotFoundException {
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
    public static void onSaveButtonPress(Stage fileStage) throws IOException {
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
    public static void onReadButtonPress() throws SerialPortException, IOException {
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
    public static void onSaveToXFMPress(ComboBox<Integer> patchPicker) throws IOException, SerialPortException {
        serialCommandHandler.writeProgram(patchPicker.getValue());
    }

    /**
     * Activates when user changes patch from the drop down menu
     *
     * @param value
     * @throws SerialPortException
     * @throws IOException
     */
    public static void onPatchPicked(int value) throws SerialPortException, IOException {
        serialCommandHandler.setLIVE_CHANGES(false);
        System.out.println("Getting patch number " + value);
        serialCommandHandler.readProgram(value);
        setAllIntFieldValues(serialCommandHandler.getAllValues());
        serialCommandHandler.setLIVE_CHANGES(true);
    }

    public static void onMidiChannelChange(UNIT_NUMBER unit, int channel) throws IOException, SerialPortException {
        serialCommandHandler.setMidiChannel(unit, channel);
    }

    /**
     * Sets all of the parameter field values to its corresponding position in the dump array
     * Called when changing patch/program
     *
     * @param dump The 512 length byte array containing all parameters.
     */
    public static void setAllIntFieldValues(byte[] dump) {
        // int offset = 48;
        if (dump.length == 512) {
            for (IntField intField : paramFields) {
                // int paramAddress = Integer.parseInt(intField.getId()) + offset;
                intField.setValue(dump[Integer.parseInt(intField.getId())] & 0xff);
            }
        }
    }

    public static void setUnit(UNIT_NUMBER unit_number) throws IOException, SerialPortException {
        serialCommandHandler.setUnit(unit_number);
        System.out.println("Active Unit changed to " + unit_number);
    }

    public static void onLiveChanged(boolean live){
        serialCommandHandler.setLIVE_CHANGES(live);
    }
}
