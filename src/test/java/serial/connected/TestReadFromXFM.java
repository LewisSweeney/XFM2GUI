package serial.connected;

import javafx.application.Platform;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestReadFromXFM {
    static String os = System.getProperty("os.name").toLowerCase();

    static SerialPort jsscSerialPort;
    static com.fazecast.jSerialComm.SerialPort jSerialCommPort;
    static SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

    @BeforeAll
    public static void initialise() {

        // Gets the correct serial port depending on platform
        // This is tested this way to avoid unnecessary failing tests on different platforms
        if (os.contains("mac") || os.contains("darwin")) {
            // Direct reference to the XFM2 device I am using - would need changing on another machine
            jsscSerialPort = new SerialPort("/dev/tty.usbserial-210328AD3A891");
        } else if (os.contains("win")){
            com.fazecast.jSerialComm.SerialPort[] jSerialComms = com.fazecast.jSerialComm.SerialPort.getCommPorts();
            for(com.fazecast.jSerialComm.SerialPort port:jSerialComms){
                if(port.getSystemPortName().equals("COM6")){
                    jSerialCommPort = port;
                }
            }
        } else{
            // Again, direct reference, although more likely to be correct if only one device is connected...
            jsscSerialPort = new SerialPort("/dev/ttyUSB1");
        }
        serialHandlerBridge.setSerialPort(jsscSerialPort);

    }

    @Test
    public void readingXFM2IntoEmptyByteArrayWillGet512BytesOfData() throws IOException, SerialPortException {
        byte[] data = serialHandlerBridge.getAllValues();
        assertEquals(data.length,512,"Data received from connected device should be 512 bytes long");
    }
}
