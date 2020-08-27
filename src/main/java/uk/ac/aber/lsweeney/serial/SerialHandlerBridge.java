package uk.ac.aber.lsweeney.serial;

import jssc.SerialPort;
import jssc.SerialPortException;
import uk.ac.aber.lsweeney.enums.ALERT_TYPE;
import uk.ac.aber.lsweeney.enums.LIBRARY_CHOICE;
import uk.ac.aber.lsweeney.enums.UNIT_NUMBER;
import uk.ac.aber.lsweeney.functionhandlers.AlertHandler;
import uk.ac.aber.lsweeney.serial.other.SerialHandlerJSSC;
import uk.ac.aber.lsweeney.serial.windows.SerialHandlerJSerialComm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SerialHandlerBridge {

    LIBRARY_CHOICE library_choice = LIBRARY_CHOICE.JSERIALCOMM;
    static SerialHandlerBridge SINGLE_INSTANCE = new SerialHandlerBridge();

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
     * @throws SerialPortException  Serial port may not be found
     * @throws InterruptedException When thread sleeps it may be interrupted
     * @throws IOException          Data may fail to be read from device or written to BAOS
     */
    public void setIndividualValue(int paramNeeded, Integer value) throws SerialPortException, InterruptedException, IOException {
        byte[] bytes;
        Thread.sleep(4);
        if (paramNeeded > 254) {
            int secondByte = 255;
            int thirdByte = paramNeeded - 255;
            bytes = new byte[4];
            bytes[0] = 's';
            bytes[1] = (byte) secondByte;
            bytes[2] = (byte) thirdByte;
            bytes[3] = value.byteValue();
        } else {
            bytes = new byte[4];
            bytes[0] = 's';
            bytes[1] = (byte) paramNeeded;
            bytes[2] = value.byteValue();
            bytes[3] = 0;
        }


        sendCommand(bytes, 0, false);
    }

    /**
     * Returns all current parameter values as a byte[512]
     *
     * @return byte array from the device
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
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
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
     */
    public void setAllValues(byte[] values) throws IOException, SerialPortException {
        if (values.length == 512) {
            byte[] bytes = new byte[1];

            bytes[0] = 'j';

            sendCommand(bytes, 0, false);
            sendCommand(values, 0, false);
        }
    }

    /**
     * Changes the synthesizer unit being worked on depending on user selection
     *
     * @param unit_number Unit number zero or one, corresponds to unit number on device
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
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
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
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
     * @param layering boolean that corresponds to numerical value which is sent to board
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
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
        sendCommand(bytes, 0, false);
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
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
     */
    public void readProgram(Integer progNum) throws SerialPortException, IOException {
        if (progNum > 0 && progNum < 128) {
            byte[] bytes = new byte[2];
            bytes[0] = 'r';
            bytes[1] = progNum.byteValue();
            sendCommand(bytes, 0, false);
        }
    }

    /**
     * Takes all parameters within the application, writes them to the board, and then saves to the specified program
     *
     * @param progNum Program number to be saved over
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
     */
    public void writeProgram(Integer progNum) throws SerialPortException, IOException {

        if (progNum == null) {
            AlertHandler alertHandler = new AlertHandler();
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
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
     */
    @SuppressWarnings("unused")
    public void initEeprom() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = '$';
        sendCommand(bytes, 0, false);
    }

    /**
     * @param file The file to be written to board
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
     */
    public void writeTunings(File file) throws IOException, SerialPortException {
        writeFile('#', 't', file, 512);
    }

    /**
     * @param file The file to be written to board
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
     */
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
        } catch (IOException ignored) {
        }

        if (fp != 65536) {
            return;
        }

        for (int i = 0; i < 128; i++) {
            byte[] prog = new byte[512];

            System.arraycopy(filebuf, i * 512, prog, 0, 512);
            setAllValues(prog);
            writeProgram(i);
        }


    }

    /**
     * @param file The file to be written to board
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
     */
    @SuppressWarnings("SameParameterValue")
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

        System.out.println("ERASING FLASH");

        byte[] flash = sendCommand(bytes, 1, true);

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
        } catch (IOException ignored) {
        }

        byte[] expected = new byte[1];
        expected[0] = -1;

        System.out.println("FILE SIZE = " + bufferSize * 256);


        for (int i = 0; i < bufferSize; ++i) {

            byte[] send = new byte[256];

            for (int j = 0; j < 256; j++) {
                send[j] = filebuf[j + i * 256];
                System.out.println(send[j]);
            }

            System.out.println("BUFFER " + i);

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
     * Generic method that sends the given bytes to the board, using the appropriate library
     * Used for all command methods
     *
     * @param bytes            byte[] that contains the bytes to be sent to the board
     * @param expectedDataBits Integer that dictates how long the method should hang to find relevant data from board (jSerialComm)
     * @param finalByte        Whether or not this is the final byte being sent as part of a chain of commands (JSSC)
     * @return returns any data that the board replies with
     * @throws SerialPortException Serial port may not be found
     * @throws IOException         Data may fail to be read from device or written to BAOS
     */
    private byte[] sendCommand(byte[] bytes, int expectedDataBits, boolean finalByte) throws SerialPortException, IOException {
        byte[] data = null;
        switch (library_choice) {
            case JSSC -> data = serialHandlerJSSC.sendCommand(bytes, finalByte);
            case JSERIALCOMM -> data = serialHandlerJSerialComm.sendCommand(bytes, expectedDataBits);
        }
        return data;
    }

    /**
     * -UNUSED-
     * <p>
     * Intended to be used to make all commands async, but caused GUI update issues
     * Would have been used for all command methods
     *
     * @param bytes            byte[] that contains the bytes to be sent to the board
     * @param expectedDataBits The number of bits the method that called it is expecting back (used mainly with jSerialComm)
     * @return returns any data that the board replies with
     */
    @SuppressWarnings("unused")
    private byte[] sendAsyncCommand(byte[] bytes, int expectedDataBits) {
        final byte[][] data = {null};
        switch (library_choice) {
            case JSSC -> {
                Runnable r = () -> {
                    try {
                        data[0] = serialHandlerJSSC.sendCommand(bytes, false);
                    } catch (SerialPortException | IOException e) {
                        e.printStackTrace();
                    }
                };

            }
            case JSERIALCOMM -> {
                Runnable r = () -> data[0] = serialHandlerJSerialComm.sendCommand(bytes, expectedDataBits);
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

}
