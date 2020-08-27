package serial.notXFM2;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestNotXFM2ChangePatch {

    static String os = System.getProperty("os.name").toLowerCase();

    static SerialPort jsscSerialPort;
    static com.fazecast.jSerialComm.SerialPort jSerialCommPort;
    static SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

    Random random = new Random();

    @BeforeAll
    public static void initialise() {

        // Gets the correct serial port depending on platform
        // This is tested this way to avoid unnecessary failing tests on different platforms
        if (os.contains("mac") || os.contains("darwin")) {
            // Direct reference to the XFM2 device I am using - would need changing on another machine
            jsscSerialPort = new SerialPort("/dev/tty.usbmodem14201");
        } else if (os.contains("win")){
            com.fazecast.jSerialComm.SerialPort[] jSerialComms = com.fazecast.jSerialComm.SerialPort.getCommPorts();
            for(com.fazecast.jSerialComm.SerialPort port:jSerialComms){
                if(port.getSystemPortName().equals("COM3")){
                    jSerialCommPort = port;
                }
            }
        } else{
            // Again, direct reference, although more likely to be correct if only one device is connected...
            jsscSerialPort = new SerialPort("/dev/ttyACM0");
        }
        serialHandlerBridge.setSerialPort(jsscSerialPort);
    }

    @Test
    public void ReadingProgramsWhenDisconnectedResultsInNoChange() throws IOException, SerialPortException {

        serialHandlerBridge.readProgram(1);
        byte[] initData = serialHandlerBridge.getAllValues();
        serialHandlerBridge.readProgram(12);
        byte[] newData = serialHandlerBridge.getAllValues();

        boolean differenceBetweenTwoDataSets = false;
        for(int i = 0;i < initData.length;i++){
            if(differenceBetweenTwoDataSets){
                break;
            } else{
                if(initData[i] != newData[i]){
                    differenceBetweenTwoDataSets = true;
                }
            }
        }

        assertFalse(differenceBetweenTwoDataSets, "There should be no difference between the datasets");
    }
}
