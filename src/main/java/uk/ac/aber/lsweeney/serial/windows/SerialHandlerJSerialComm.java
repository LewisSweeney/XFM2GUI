package uk.ac.aber.lsweeney.serial.windows;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;

public class SerialHandlerJSerialComm {
    SerialPort serialPort;
    private static final int BAUD_RATE = 500000;


    public SerialHandlerJSerialComm(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    /**
     * Generic method that sends the given bytes to the board
     * Used for all command methods
     *
     * @param bytes byte[] that contains the bytes to be sent to the board
     * @return returns any data that the board replies with
     */
    public byte[] sendCommand(byte[] bytes, int expectedDataBits) {
        if (serialPort != null) {

            if (!serialPort.isOpen()) {
                serialPort.openPort();
            }

            serialPort.writeBytes(bytes, bytes.length);
            byte[] data = null;
            if(expectedDataBits > 0) {
                data = getData(expectedDataBits);
            }
            serialPort.closePort();
            return data;
        } else {
            return new byte[1];
        }

    }

    /**
     * Method used to get data from the device once a command has been sent to the board
     *
     * @return byte[] which is translated by the program for display in the GUI
     */
    @SuppressWarnings("StatementWithEmptyBody")
    byte[] getData(int expectedDataBits) {
        ArrayList<Byte> byteArrayList = new ArrayList<>();

        if (serialPort == null) {
            return null;
        }

        while (serialPort.bytesAvailable() < expectedDataBits) {
            // Empty to wait for data to be available
        }


        while (serialPort.bytesAvailable() > 0) {
         // try {
          //    Thread.sleep(1);
        //  } catch (InterruptedException ex) {}
          byte newByte = readSingleByte();
          byteArrayList.add(newByte);
        }

        byte[] data = new byte[byteArrayList.size()];

        for (int i = 0; i < byteArrayList.size(); i++) {
            data[i] = byteArrayList.get(i);
        }

        return data;
    }

    private byte readSingleByte() {
        byte[] buffer = new byte[1];
        serialPort.readBytes(buffer, 1);
        return buffer[0];
    }

    // GETTERS AND SETTERS
    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
        if (this.serialPort != null) {
            this.serialPort.setComPortParameters(BAUD_RATE, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        }
    }

}
