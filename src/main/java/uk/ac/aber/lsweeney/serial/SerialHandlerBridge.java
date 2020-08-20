package uk.ac.aber.lsweeney.serial;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.apache.commons.lang3.SystemUtils;
import uk.ac.aber.lsweeney.enums.ALERT_TYPE;
import uk.ac.aber.lsweeney.enums.LIBRARY_CHOICE;
import uk.ac.aber.lsweeney.enums.UNIT_NUMBER;
import uk.ac.aber.lsweeney.functionhandlers.AlertHandler;
import uk.ac.aber.lsweeney.serial.other.SerialHandlerJSSC;
import uk.ac.aber.lsweeney.serial.windows.SerialHandlerJSerialComm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerialHandlerBridge {

    byte[] initData = null;

    LIBRARY_CHOICE library_choice = LIBRARY_CHOICE.JSERIALCOMM;
    static SerialHandlerBridge SINGLE_INSTANCE = new SerialHandlerBridge();

    AlertHandler alertHandler = new AlertHandler();

    SerialHandlerJSSC serialHandlerJSSC = new SerialHandlerJSSC(null);
    SerialHandlerJSerialComm serialHandlerJSerialComm = new SerialHandlerJSerialComm(null);

    private SerialHandlerBridge(){
       String os = SystemUtils.OS_NAME.toLowerCase();

       if(os.contains("mac")){
           library_choice = LIBRARY_CHOICE.JSSC;
       }
    }


    /**
     * Used for setting an individual value on the XFM2.
     * Happens when program is in "Live Changes" mode
     * @param paramNeeded The ID of the param being changed
     * @param value The value the param is being set to
     * @throws SerialPortException
     * @throws InterruptedException
     * @throws IOException
     */
    public void setIndividualValue(int paramNeeded, Integer value) throws SerialPortException, InterruptedException, IOException {
        byte[] bytes;
        Thread.sleep(10);
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


        sendCommand(bytes,0);
    }

    /**
     * Returns all current parameter values as a byte[512]
     * @return
     * @throws SerialPortException
     * @throws IOException
     */
    public byte[] getAllValues() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = 'd';

        return sendCommand(bytes, 512);
    }

    /**
     * Takes values from all parameter fields, inserts them into a byte[512] and sends them to the board
     * @param values The values from the parameter fields
     * @throws IOException
     * @throws SerialPortException
     */
    public void setAllValues(byte[] values) throws IOException, SerialPortException {
        byte[] bytes = new byte[1];

        bytes[0] = 'j';

        sendCommand(bytes,0);
        sendCommand(values,0);
    }

    /**
     * Changes the synthesizer unit being worked on depending on user selection
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

        sendCommand(bytes,0);

    }

    /**
     * Sets the MIDI channel paramter value for the unit (unit_number) to the value sent (midiChannel)
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
        sendCommand(bytes,0);
        initializeCurrentProgram();

    }

    /**
     * Sets the MIDI Layering parameter to 0 or 1 depending on the passed boolean
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
        byte[] data = sendCommand(bytes,0);
    }

    // UNUSED
    public void initializeCurrentProgram() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = 'i';
        sendCommand(bytes,0);
    }

    /**
     * Changes the program currently loaded into XFM memory to the one selected from the menu
     * @param progNum Program number to be set
     * @throws SerialPortException
     * @throws IOException
     */
    public void readProgram(Integer progNum) throws SerialPortException, IOException {
        if (progNum > 0 && progNum < 128) {
            byte[] bytes = new byte[2];
            bytes[0] = 'r';
            bytes[1] = progNum.byteValue();
            byte[] data = sendCommand(bytes,0);
        }
    }

    /**
     * Takes all parameters within the application, writes them to the board, and then saves to the specified program
     * @param progNum Program number to be saved over
     * @throws SerialPortException
     * @throws IOException
     */
    public void writeProgram(Integer progNum) throws SerialPortException, IOException {
        if(progNum == null){
            alertHandler.SendAlert(ALERT_TYPE.NO_PATCH_CHOSEN);
            return;
        }
        if (progNum > 0 && progNum < 128) {
            System.out.println("WRITING PROGRAM");
            byte[] bytes = new byte[2];
            bytes[0] = 'w';
            bytes[1] = progNum.byteValue();
            sendCommand(bytes,0);
        }
    }

    /**
     * -UNUSED-
     * Deletes all programs from the eeprom, setting all parameter values to zero
     * @throws SerialPortException
     * @throws IOException
     */
    public void initEeprom() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = '$';
        sendCommand(bytes,0);
    }

    /**
     * Generic method that sends the given bytes to the board
     * Used for all command methods
     * @param bytes byte[] that contains the bytes to be sent to the board
     * @return returns any data that the board replies with
     * @throws SerialPortException
     * @throws IOException
     */
    private byte[] sendCommand(byte[] bytes, int expectedDataBits) throws SerialPortException, IOException {
        byte[] data = null;
        switch(library_choice){
            case JSSC -> {
                data = serialHandlerJSSC.sendCommand(bytes);
            }
            case JSERIALCOMM-> {
                data = serialHandlerJSerialComm.sendCommand(bytes, expectedDataBits);
            }
        }

        return data;
    }

    public void setSerialPort(SerialPort newPort){
        serialHandlerJSSC.setSerialPort(newPort);
    }

    public void setSerialPort(com.fazecast.jSerialComm.SerialPort newPort){
        serialHandlerJSerialComm.setSerialPort(newPort);
    }

    public LIBRARY_CHOICE getLibrary_choice(){
        return library_choice;
    }

    public static SerialHandlerBridge getSINGLE_INSTANCE(){
        return SINGLE_INSTANCE;
    }

    public boolean isThereASerialPort(){

        boolean serialPortExist = false;

        switch (library_choice){
            case JSSC ->{
                if(serialHandlerJSSC.getSerialPort() != null){
                    serialPortExist = true;
                }
            }
            case JSERIALCOMM -> {
                if(serialHandlerJSerialComm.getSerialPort() != null){
                    serialPortExist = true;
                }
            }
        }
        return serialPortExist;
    }

}
