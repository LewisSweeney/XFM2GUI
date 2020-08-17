package uk.ac.aber.lsweeney.serial.other;


import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import uk.ac.aber.lsweeney.enums.ALERT_TYPE;
import uk.ac.aber.lsweeney.enums.UNIT_NUMBER;
import uk.ac.aber.lsweeney.functionhandlers.AlertHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Handles all serial-over-USB communications, using JSSC to send byte arrays as commands
 */
public class SerialHandlerOther {
    private static SerialPort serialPort;
    private static final int BAUD_RATE = 500000;
    AlertHandler alertHandler = new AlertHandler();


    public SerialHandlerOther(SerialPort serialPort) {
        SerialHandlerOther.serialPort = serialPort;
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


        sendCommand(bytes);
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
        return sendCommand(bytes);
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

        sendCommand(bytes);
        sendCommand(values);
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

        sendCommand(bytes);

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
        byte[] currentVals = getAllValues();
        if (unit_number == UNIT_NUMBER.ZERO) {
            secondByte = 10;
        } else {
            secondByte = 11;
        }
        byte[] bytes = new byte[3];
        bytes[0] = (byte) firstByte;
        bytes[1] = (byte) secondByte;
        bytes[2] = midiChannel.byteValue();
        byte[] data = sendCommand(bytes);
        initializeCurrentProgram();
        byte[] newVals = getAllValues();

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
        byte[] data = sendCommand(bytes);
    }

    // UNUSED
    public void initializeCurrentProgram() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = 'i';
        sendCommand(bytes);
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
            byte[] data = sendCommand(bytes);

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
            byte[] bytes = new byte[2];
            bytes[0] = 'w';
            bytes[1] = progNum.byteValue();
            byte[] data = sendCommand(bytes);
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
        sendCommand(bytes);
    }

    /**
     * Generic method that sends the given bytes to the board
     * Used for all command methods
     * @param bytes byte[] that contains the bytes to be sent to the board
     * @return returns any data that the board replies with
     * @throws SerialPortException
     * @throws IOException
     */
    private byte[] sendCommand(byte[] bytes) throws SerialPortException, IOException {
        if(serialPort != null) {
            try {
                serialPort.openPort();

                serialPort.setParams(BAUD_RATE,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                        SerialPort.FLOWCONTROL_RTSCTS_OUT);

                Thread.sleep(10);
                serialPort.writeBytes(bytes);
            } catch (SerialPortException | InterruptedException ex) {
                System.out.println("There are an error on writing string to port т: " + ex);
            }
            byte[] data = getData();
            if(data == null){
                alertHandler.SendAlert(ALERT_TYPE.NOT_XFM);
            }
            serialPort.closePort();
            return data;
        } else{
          //  alertHandler.SendAlert(ALERT_TYPE.NO_DEVICE);
            return new byte[1];
        }

    }

    /**
     * Method used to get data from the device once a command has been sent to the board
     * @return byte[] which is translated by the program for display in the GUI
     * @throws SerialPortException
     * @throws IOException
     */
    byte[] getData() throws SerialPortException, IOException {
        if(serialPort == null){
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] b;

        try {
            while ((b = serialPort.readBytes(1, 500)) != null) {
                byteArrayOutputStream.write(b);
            }
        } catch (SerialPortTimeoutException ex) {
            System.out.println("Serial Port Timed Out");
        }
        return byteArrayOutputStream.toByteArray();
    }

    // GETTERS AND SETTERS
    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        SerialHandlerOther.serialPort = serialPort;
    }



}
