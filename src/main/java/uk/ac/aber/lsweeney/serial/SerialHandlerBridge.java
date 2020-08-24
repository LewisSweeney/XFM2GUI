package uk.ac.aber.lsweeney.serial;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import jssc.SerialPort;
import jssc.SerialPortException;
import uk.ac.aber.lsweeney.enums.ALERT_TYPE;
import uk.ac.aber.lsweeney.enums.LIBRARY_CHOICE;
import uk.ac.aber.lsweeney.enums.UNIT_NUMBER;
import uk.ac.aber.lsweeney.functionhandlers.AlertHandler;
import uk.ac.aber.lsweeney.serial.other.SerialHandlerJSSC;
import uk.ac.aber.lsweeney.serial.windows.SerialHandlerJSerialComm;

import java.io.*;

public class SerialHandlerBridge {

    byte[] initData = null;

    LIBRARY_CHOICE library_choice = LIBRARY_CHOICE.JSERIALCOMM;
    static SerialHandlerBridge SINGLE_INSTANCE = new SerialHandlerBridge();

    AlertHandler alertHandler = new AlertHandler();

    ProgressIndicator progress = new ProgressIndicator(-1);
    Label progressLabel = new Label("Erasing Flash...");

    SerialHandlerJSSC serialHandlerJSSC = new SerialHandlerJSSC(null);
    SerialHandlerJSerialComm serialHandlerJSerialComm = new SerialHandlerJSerialComm(null);

    private SerialHandlerBridge() {

        String test = System.getProperty("os.name").toLowerCase();

        if (!test.contains("windows")) {
            System.out.println("JSSC");
            library_choice = LIBRARY_CHOICE.JSSC;
        }
    }


    /**
     * Used for setting an individual value on the XFM2.
     * Happens when program is in "Live Changes" mode
     *
     * @param paramNeeded The ID of the param being changed
     * @param value       The value the param is being set to
     * @throws SerialPortException
     * @throws InterruptedException
     * @throws IOException
     */
    public void setIndividualValue(int paramNeeded, Integer value) throws SerialPortException, InterruptedException, IOException {
        byte[] bytes;
        Thread.sleep(5);
        if (paramNeeded > 255) {
            int secondByte = 255;
            int thirdByte = paramNeeded - 256;
            bytes = new byte[4];
            bytes[0] = 's';
            bytes[1] = (byte) secondByte;
            bytes[2] = (byte) thirdByte;
            bytes[3] = value.byteValue();
        } else {
            bytes = new byte[3];
            bytes[0] = 's';
            bytes[1] = (byte) paramNeeded;
            bytes[2] = value.byteValue();
            //bytes[3] = 0;
        }


        sendCommand(bytes, 0, false);
    }

    /**
     * Returns all current parameter values as a byte[512]
     *
     * @return
     * @throws SerialPortException
     * @throws IOException
     */
    public byte[] getAllValues() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = 'd';

        return sendCommand(bytes, 512, false);
    }

    /**
     * Takes values from all parameter fields, inserts them into a byte[512] and sends them to the board
     *
     * @param values The values from the parameter fields
     * @throws IOException
     * @throws SerialPortException
     */
    public void setAllValues(byte[] values) throws IOException, SerialPortException {
        byte[] bytes = new byte[1];

        bytes[0] = 'j';

        sendCommand(bytes, 0, false);
        sendCommand(values, 0, false);
    }

    /**
     * Changes the synthesizer unit being worked on depending on user selection
     *
     * @param unit_number
     * @throws SerialPortException
     * @throws IOException
     */
    public void setUnit(UNIT_NUMBER unit_number) throws SerialPortException, IOException {
        int command;
        if (unit_number == UNIT_NUMBER.ONE) {
            command = 1;
        } else {
            command = 2;
        }
        byte[] bytes = new byte[1];
        bytes[0] = (byte) command;

        sendCommand(bytes, 0, false);

    }

    /**
     * Sets the MIDI channel paramter value for the unit (unit_number) to the value sent (midiChannel)
     *
     * @param unit_number Enum for choosing unit ZERO or ONE
     * @param midiChannel int 0-16, where 0 = omni setting on the device, and 1-16 correspond to channel numbers
     * @throws SerialPortException
     * @throws IOException
     */
    public void setMidiChannel(UNIT_NUMBER unit_number, Integer midiChannel) throws SerialPortException, IOException {
        int firstByte = '*';
        int secondByte;
        if (unit_number == UNIT_NUMBER.ZERO) {
            secondByte = 10;
        } else {
            secondByte = 11;
        }
        byte[] bytes = new byte[3];
        bytes[0] = (byte) firstByte;
        bytes[1] = (byte) secondByte;
        bytes[2] = midiChannel.byteValue();
        sendCommand(bytes, 0, false);
        initializeCurrentProgram();

    }

    /**
     * Sets the MIDI Layering parameter to 0 or 1 depending on the passed boolean
     *
     * @param layering boolean that corresponds to numerical value which is sent ot board
     * @throws SerialPortException
     * @throws IOException
     */
    public void setMidiLayering(boolean layering) throws SerialPortException, IOException {
        int firstByte = 42;
        int secondByte = 12;
        int thirdByte = 0;
        byte[] bytes = new byte[3];
        bytes[0] = (byte) firstByte;
        bytes[1] = (byte) secondByte;
        if (layering) {
            thirdByte = 1;
        }
        bytes[2] = (byte) thirdByte;
        byte[] data = sendCommand(bytes, 0, false);
    }

    // UNUSED
    public void initializeCurrentProgram() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = 'i';
        sendCommand(bytes, 0, false);
    }

    /**
     * Changes the program currently loaded into XFM memory to the one selected from the menu
     *
     * @param progNum Program number to be set
     * @throws SerialPortException
     * @throws IOException
     */
    public void readProgram(Integer progNum) throws SerialPortException, IOException {
        if (progNum > 0 && progNum < 128) {
            byte[] bytes = new byte[2];
            bytes[0] = 'r';
            bytes[1] = progNum.byteValue();
            byte[] data = sendCommand(bytes, 0, false);
        }
    }

    /**
     * Takes all parameters within the application, writes them to the board, and then saves to the specified program
     *
     * @param progNum Program number to be saved over
     * @throws SerialPortException
     * @throws IOException
     */
    public void writeProgram(Integer progNum) throws SerialPortException, IOException {
        if (progNum == null) {
            alertHandler.sendAlert(ALERT_TYPE.NO_PATCH_CHOSEN);
            return;
        }
        if (progNum > 0 && progNum < 128) {
            System.out.println("WRITING PROGRAM");
            byte[] bytes = new byte[2];
            bytes[0] = 'w';
            bytes[1] = progNum.byteValue();
            sendCommand(bytes, 0, false);
        }
    }

    /**
     * -UNUSED-
     * Deletes all programs from the eeprom, setting all parameter values to zero
     *
     * @throws SerialPortException
     * @throws IOException
     */
    public void initEeprom() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = '$';
        sendCommand(bytes, 0, false);
    }

    public void writeTunings(File file) throws IOException, SerialPortException {

        loadingBox().show();

        writeFile('#', 't', file, 512);

        while (progress.getProgress() < 1) {

        }

        while (progress.getProgress() >= 1.0) {
            loadingBox().close();
            return;
        }

    }

    public void writeBank(File file) throws IOException, SerialPortException {
        FileInputStream fs;

        if (file != null) {
            fs = new FileInputStream(file);
        } else {
            System.out.println("FILE ERROR");
            return;
        }

        byte[] filebuf = new byte[65536];

        for (int f = 0; f < 65536; f++) {
            filebuf[f] = 0;
        }

        int fp = 0;
        try {
            while (fs.available() > 0) {
                int test = fs.read();
                System.out.println("Int value = " + test);
                filebuf[fp] = (byte) test;
                fp++;
            }
        } catch (IOException ex) {
        }

        if (fp != 65536) {
            return;
        }

        for (int i = 0; i < 128; i++) {
            byte[] prog = new byte[512];
            for (int j = 0; j < 512; j++) {
                prog[j] = filebuf[j + i * 512];
            }
            setAllValues(prog);
            writeProgram(i);
        }


    }

    private void writeFile(char init, char start, File file, int bufferSize) throws IOException, SerialPortException {
        byte[] bytes = new byte[1];

        FileInputStream fs;

        if (file != null) {
            fs = new FileInputStream(file);
        } else {
            System.out.println("FILE ERROR");
            return;
        }

        bytes[0] = (byte) init;
        byte[] flash = new byte[1];


        flash = sendCommand(bytes, 1, true);

        if (flash[0] != 0) {
            System.out.println("ERROR ON FLASH");
            return;
        } else {
            System.out.println("FLASH ERASED");
        }

        bytes[0] = (byte) start;

        sendCommand(bytes, 0, false);


        int FILE_SIZE = bufferSize * 256;
        byte[] filebuf = new byte[FILE_SIZE];

        for (int f = 0; f < FILE_SIZE; f++) {
            filebuf[f] = 0;
        }

        int fp = 0;
        try {
            while (fs.available() > 0) {
                filebuf[fp] = (byte) fs.read();
                fp++;
            }
        } catch (IOException ex) {
        }

        byte[] expected = new byte[1];
        expected[0] = -1;

        for (int i = 0; i < bufferSize; ++i) {

            byte[] send = new byte[256];

            for (int j = 0; j < 256; j++) {
                send[j] = filebuf[j + i * 256];
            }

            if (i == bufferSize - 1) {

                expected = sendCommand(send, 1, true);

            } else {
                sendCommand(send, 0, false);
            }

        }

        if (expected[0] != 0) {
            System.out.println("ERROR ON WRITING FILE");
        }

    }

    /**
     * Generic method that sends the given bytes to the board
     * Used for all command methods
     *
     * @param bytes byte[] that contains the bytes to be sent to the board
     * @return returns any data that the board replies with
     * @throws SerialPortException
     * @throws IOException
     */
    private byte[] sendCommand(byte[] bytes, int expectedDataBits, boolean finalByte) throws SerialPortException, IOException {
        byte[][] data = {null};
        switch (library_choice) {
            case JSSC -> {
                data[0] = serialHandlerJSSC.sendCommand(bytes, finalByte);
            }
            case JSERIALCOMM -> {
                data[0] = serialHandlerJSerialComm.sendCommand(bytes, expectedDataBits);
            }
        }
        return data[0];
    }

    /**
     * Generic method that sends the given bytes to the board
     * Used for all command methods
     *
     * @param bytes byte[] that contains the bytes to be sent to the board
     * @return returns any data that the board replies with
     * @throws SerialPortException
     * @throws IOException
     */
    private byte[] sendAsyncCommand(byte[] bytes, int expectedDataBits) throws SerialPortException, IOException {
        final byte[][] data = {null};
        switch (library_choice) {
            case JSSC -> {
                Runnable r = new Runnable() {
                    public void run() {
                        try {
                            data[0] = serialHandlerJSSC.sendCommand(bytes, false);
                        } catch (SerialPortException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

            }
            case JSERIALCOMM -> {
                Runnable r = new Runnable() {
                    public void run() {
                        try {
                            data[0] = serialHandlerJSerialComm.sendCommand(bytes, expectedDataBits);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
        }

        return data[0];
    }


    public void setSerialPort(SerialPort newPort) {
        serialHandlerJSSC.setSerialPort(newPort);
    }

    public void setSerialPort(com.fazecast.jSerialComm.SerialPort newPort) {
        serialHandlerJSerialComm.setSerialPort(newPort);
    }

    public LIBRARY_CHOICE getLibrary_choice() {
        return library_choice;
    }

    public static SerialHandlerBridge getSINGLE_INSTANCE() {
        return SINGLE_INSTANCE;
    }

    public boolean isThereASerialPort() {

        boolean serialPortExist = false;

        switch (library_choice) {
            case JSSC -> {
                if (serialHandlerJSSC.getSerialPort() != null) {
                    serialPortExist = true;
                }
            }
            case JSERIALCOMM -> {
                if (serialHandlerJSerialComm.getSerialPort() != null) {
                    serialPortExist = true;
                }
            }
        }
        return serialPortExist;
    }

    private Alert loadingBox() {

        VBox progBox = new VBox(progressLabel, progress);
        progBox.setAlignment(Pos.CENTER);

        Alert loading = new Alert(Alert.AlertType.NONE);
        loading.initStyle(StageStyle.UNDECORATED);
        loading.setGraphic(progBox);
        return loading;
    }


}
