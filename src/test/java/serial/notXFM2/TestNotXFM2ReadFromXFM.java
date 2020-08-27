package serial.notXFM2;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestNotXFM2ReadFromXFM {
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
            serialHandlerBridge.setSerialPort(jsscSerialPort);
            assertNotNull(jsscSerialPort);
        } else if (os.contains("win")){
            com.fazecast.jSerialComm.SerialPort[] jSerialComms = com.fazecast.jSerialComm.SerialPort.getCommPorts();
            for(com.fazecast.jSerialComm.SerialPort port:jSerialComms){
                if(port.getSystemPortName().equals("COM3")){
                    jSerialCommPort = port;
                }
            }
            serialHandlerBridge.setSerialPort(jSerialCommPort);
            assertNotNull(jSerialCommPort);
        } else{
            // Again, direct reference, although more likely to be correct if only one device is connected...
            jsscSerialPort = new SerialPort("/dev/ttyACM0");
            serialHandlerBridge.setSerialPort(jsscSerialPort);
            assertNotNull(jsscSerialPort);
        }
    }



    @Test
    public void readingWithNoDeviceResultsInOneLengthDataArray() throws IOException, SerialPortException {
        byte[] data = serialHandlerBridge.getAllValues();
        assertNotEquals(data.length,512,"Serial Handler may return an array of data that it gets from the wrongly connected device, but will not be 512 bytes");
    }
}
