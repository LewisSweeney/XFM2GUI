package main.java.serial;

import jssc.SerialPort;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import main.java.utilities.ByteToString;

import javax.sound.midi.MidiChannel;

public class SerialCommandHandler {
    private static SerialPort serialPort;

    public SerialCommandHandler(SerialPort serialPort) {
        SerialCommandHandler.serialPort = serialPort;
    }

    public String getIndividualValue(int paramNeeded){
        String indValue = "DEFAULT";

        StringBuilder command = new StringBuilder();

        if(paramNeeded > 255){
            command.append("g ").append(255).append(" ").append(paramNeeded - 255);
        } else{
            command.append("g ").append(paramNeeded);
        }

        sendCommand(command.toString());

        return indValue;
    }

    public void setIndividualValue(int paramNeeded, int value){
        StringBuilder command = new StringBuilder();

        if(paramNeeded > 255){
            command.append("s ").append(255).append(" ").append(paramNeeded - 255).append(" ").append(value);
        } else{
            command.append("s ").append(paramNeeded).append(" ").append(value);
        }

        sendCommand(command.toString());
    }

    public void getAllValues(){
        String command = "d";
        sendCommand(command);
    }

    public void setUnit(UNIT_NUMBER unit_number){
        String command;
        if (unit_number == UNIT_NUMBER.ONE) {
            command = "1";
        } else {
            command = "2";
        }

        sendCommand(command);

    }

    public void setMidiChannel(UNIT_NUMBER unit_number, int midiChannel){
        StringBuilder command = new StringBuilder();
        command.append("42 ");
        if(unit_number == UNIT_NUMBER.ONE){
            command.append("10 ").append(midiChannel);
        } else {
            command.append("11 ").append(midiChannel);
        }
        sendCommand(command.toString());
    }

    public void setMidiLayering(boolean layering){
        StringBuilder command = new StringBuilder();
        command.append("42 12 ");
        if(layering){
            command.append(1);
        } else{
            command.append(0);
        }
        sendCommand(command.toString());
    }

    public void initializeCurrentProgram(){
        sendCommand("i");
    }

    public void readProgram(int progNum){
        if(progNum > 0 && progNum <128){
            sendCommand("r " + progNum);
        }
    }

    public void writeProgram(int progNum){
        if(progNum > 0 && progNum <128){
            sendCommand("w " + progNum);
        }
    }

    public void initEeprom(){
        sendCommand("$");
    }

    private void sendCommand(String command){

    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        SerialCommandHandler.serialPort = serialPort;
    }

    static class SerialEventListener implements SerialPortEventListener {

        @Override
        public void serialEvent(jssc.SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    byte[] receivedData = serialPort.readBytes(event.getEventValue());
                    ByteToString byteToString = new ByteToString();
                    System.out.println("Byte Array Size = " + receivedData.length);
                    System.out.println("Received response: " + byteToString.byteToString(receivedData));
                }
                catch (SerialPortException ex) {
                    System.out.println("Error in receiving bytes from COM-port: " + ex);
                }
            }
        }
    }
}
