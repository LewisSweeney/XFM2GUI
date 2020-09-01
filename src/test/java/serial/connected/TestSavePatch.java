package serial.connected;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.github.steadiestllama.xfm2gui.serial.SerialHandlerBridge;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TestSavePatch {
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
            jsscSerialPort = new SerialPort("/dev/tty.usbserial-210328AD3A891");
            serialHandlerBridge.setSerialPort(jsscSerialPort);
        } else if (os.contains("win")) {
            com.fazecast.jSerialComm.SerialPort[] jSerialComms = com.fazecast.jSerialComm.SerialPort.getCommPorts();
            for (com.fazecast.jSerialComm.SerialPort port : jSerialComms) {
                if (port.getSystemPortName().equals("COM6")) {
                    jSerialCommPort = port;
                }
            }
            serialHandlerBridge.setSerialPort(jSerialCommPort);
        } else {
            // Again, direct reference, although more likely to be correct if only one device is connected...
            jsscSerialPort = new SerialPort("/dev/ttyUSB1");
            serialHandlerBridge.setSerialPort(jsscSerialPort);
        }
    }

    @Test
    public void ChangingAllParametersRandomlyThenSavingThemToAPatchWillWriteThemToXFM2Memory() throws IOException, SerialPortException {

        // Ensures program starts on program 1
        serialHandlerBridge.readProgram(1);
        // Generates random bytes to be inserted into device program
        byte[] generatedData = new byte[512];
        random.nextBytes(generatedData);

        // Will attempt to set the data for program to the random genned data
        serialHandlerBridge.setAllValues(generatedData);
        serialHandlerBridge.writeProgram(12);

        // Reads a different program before reading 12 to ensure difference of
        serialHandlerBridge.readProgram(2);
        byte[] tempData = serialHandlerBridge.getAllValues();

        serialHandlerBridge.readProgram(12);
        byte[] programData = serialHandlerBridge.getAllValues();

        assertArrayEquals(generatedData, programData, "Generated data should be equal to data from program 12");


    }

    @Test
    public void SavingToAPatchOutOfRangeDoesNothing() throws IOException, SerialPortException {

        // Ensures program starts on program 1
        serialHandlerBridge.readProgram(1);
        // Generates random bytes to be inserted into device program
        byte[] generatedData = new byte[512];
        random.nextBytes(generatedData);

        // Will attempt to set the data for program to the random genned data
        serialHandlerBridge.setAllValues(generatedData);
        serialHandlerBridge.writeProgram(12);

        // Reads a different program before reading 12 to ensure difference of
        serialHandlerBridge.readProgram(2);
        byte[] tempData = serialHandlerBridge.getAllValues();

        serialHandlerBridge.readProgram(12);
        byte[] programData = serialHandlerBridge.getAllValues();

        assertNotEquals(tempData, programData);

        assertArrayEquals(generatedData, programData, "Generated data should be equal to data from program 12");


    }

}
