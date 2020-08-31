package uk.ac.aber.lsweeney.serial.other;


import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import uk.ac.aber.lsweeney.enums.ALERT_TYPE;
import uk.ac.aber.lsweeney.functionhandlers.AlertHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Handles all serial-over-USB communications, using JSSC to send byte arrays as commands
 */
public class SerialHandlerJSSC {
    private static SerialPort serialPort;
    private static final int BAUD_RATE = 500000;



    public SerialHandlerJSSC(SerialPort serialPort) {
        SerialHandlerJSSC.serialPort = serialPort;
    }

    /**
     * Generic method that sends the given bytes to the board
     * Used for most command methods (not file writing)
     *
     * @param bytes The bytes to be sent to the device
     * @param finalByte Whether this is the final byte being sent - used to get data if necessary
     *
     * @return Returns gotten data from getData method if required, or empty, one long byte array if not
     *
     * @throws IOException IO can fail when writing to BAOS in getData method
     * @throws SerialPortException Serial port may not be found
     */
    public byte[] sendCommand(byte[] bytes, boolean finalByte) throws SerialPortException, IOException {
        if(serialPort != null) {
            try {
                if(!serialPort.isOpened()){
                    serialPort.openPort();
                }


                serialPort.setParams(BAUD_RATE,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

                serialPort.writeBytes(bytes);
            } catch (SerialPortException ex) {
                ex.printStackTrace();
            }

            byte[] data = getData();
            if(finalByte){
                while(data.length < 1){
                    data = getData();
                }
            }

            serialPort.closePort();
            return data;
        } else{
            return new byte[1];
        }
    }

    /**
     * -UNUSED-
     *
     * @param bytes The bytes to be sent to the device
     * @param finalByte Whether this is the final byte being sent - used to get data if necessary
     *
     * @return Returns gotten data from getData method if required, or empty, one long byte array if not
     *
     * @throws IOException IO can fail when writing to BAOS in getData method
     * @throws SerialPortException Serial port may not be found
     */
    @SuppressWarnings("unused")
    public byte[] sendFileCommand(byte[] bytes, boolean finalByte) throws IOException, SerialPortException {
        if(serialPort != null) {
            try {
                if(!serialPort.isOpened()){
                    serialPort.openPort();
                }


                serialPort.setParams(BAUD_RATE,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

                Thread.sleep(1);
                serialPort.writeBytes(bytes);
            } catch (SerialPortException | InterruptedException ex) {
                ex.printStackTrace();
            }

            byte[] data = getData();
            if(finalByte){
                while (data.length < 1){
                    data = getData();
                }
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
     * @throws SerialPortException Serial Port may not be found
     * @throws IOException Byte Array Output Stream can fail to be written to
     * Adapted from an answer here: https://stackoverflow.com/questions/27884295/how-to-read-all-available-data-from-serial-connection-when-using-jssc
     */
    byte[] getData() throws SerialPortException, IOException {

        if(serialPort == null || !serialPort.isOpened()){
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] b;

        try {
            Thread.sleep(1);
            while ((b = serialPort.readBytes(1, 100)) != null) {
                byteArrayOutputStream.write(b);
            }
        } catch (SerialPortTimeoutException | InterruptedException ignored) {
        }
        return byteArrayOutputStream.toByteArray();
    }

    // GETTERS AND SETTERS
    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        SerialHandlerJSSC.serialPort = serialPort;
    }



}
