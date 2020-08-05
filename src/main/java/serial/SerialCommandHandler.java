package main.java.serial;

import jssc.SerialPort;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import main.java.utilities.ByteToString;

import javax.sound.midi.MidiChannel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerialCommandHandler {
    private static SerialPort serialPort;
    private static final int BAUD_RATE = 500000;
    private byte[] latestData;

    public Boolean LIVE_CHANGES = false;

    public SerialCommandHandler(SerialPort serialPort) {
        SerialCommandHandler.serialPort = serialPort;
    }

    public void getIndividualValue(int paramNeeded) throws SerialPortException, IOException {
        byte[] bytes;

        if (paramNeeded > 255) {
            bytes = new byte[3];
            int thirdByte = paramNeeded - 255;
            bytes[0] = 'g';
            bytes[1] = (byte) 255;
            bytes[2] = (byte) thirdByte;
        } else {
            bytes = new byte[2];
            bytes[0] = 'g';
            bytes[1] = (byte) paramNeeded;
        }

        sendCommand(bytes);
    }

    public void setIndividualValue(int paramNeeded, Integer value) throws SerialPortException, InterruptedException, IOException {
        StringBuilder command = new StringBuilder();
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

    public byte[] getAllValues() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = 'd';
        return sendCommand(bytes);
    }

    public void setAllValues(byte[] values) throws IOException, SerialPortException {
        byte[] bytes = new byte[513];

        for(byte b:bytes){
            b=0;
        }
        bytes[0] = 'j';

        if (values.length - 1 >= 0) System.arraycopy(values, 0, bytes, 1, values.length - 1);

        sendCommand(bytes);
    }

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

    public void setMidiChannel(UNIT_NUMBER unit_number, Integer midiChannel) throws SerialPortException, IOException {
        int firstByte = '*';
        int secondByte;
        if (unit_number == UNIT_NUMBER.ONE) {
            secondByte = 10;
        } else {
            secondByte = 11;
        }
        byte[] bytes = new byte[3];
        bytes[0] = (byte) firstByte;
        bytes[1] = (byte) secondByte;
        bytes[2] = midiChannel.byteValue();
        byte[] data = sendCommand(bytes);
        if(unit_number == UNIT_NUMBER.ONE){
            System.out.println("Set unit 1 channel to " + data[0]);
        } else{
            System.out.println("Set unit 2 channel to " + data[0]);
        }

    }

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
        if(data.length > 0){
            System.out.println("Set layering to " + layering);
        }

    }

    public void initializeCurrentProgram() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = 'i';
        sendCommand(bytes);
    }

    public void readProgram(Integer progNum) throws SerialPortException, IOException {
        if (progNum > 0 && progNum < 128) {
            byte[] bytes = new byte[2];
            bytes[0] = 'r';
            bytes[1] = progNum.byteValue();
            byte[] data = sendCommand(bytes);

        }
    }

    public void writeProgram(Integer progNum) throws SerialPortException, IOException {
        if (progNum > 0 && progNum < 128) {
            byte[] bytes = new byte[2];
            bytes[0] = 'w';
            bytes[1] = progNum.byteValue();
            byte[] data = sendCommand(bytes);
        }
    }

    public void initEeprom() throws SerialPortException, IOException {
        byte[] bytes = new byte[1];
        bytes[0] = '$';
        sendCommand(bytes);
    }

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
            serialPort.closePort();
            return data;
        } else{
            return new byte[1];
        }

    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        SerialCommandHandler.serialPort = serialPort;
    }

    byte[] getData() throws SerialPortException, IOException {
        if(serialPort == null){
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b;

        try {
            while ((b = serialPort.readBytes(1, 100)) != null) {
                baos.write(b);
                //                System.out.println ("Wrote: " + b.length + " bytes");
            }
            //            System.out.println("Returning: " + Arrays.toString(baos.toByteArray()));
        } catch (SerialPortTimeoutException ex) {
            //don't want to catch it, it just means there is no more data         to read
        }
        latestData = baos.toByteArray();
        return baos.toByteArray();
    }

    public Boolean getLIVE_CHANGES() {
        return LIVE_CHANGES;
    }

    public void setLIVE_CHANGES(boolean LIVE_CHANGES){
        this.LIVE_CHANGES = LIVE_CHANGES;
    }



}
