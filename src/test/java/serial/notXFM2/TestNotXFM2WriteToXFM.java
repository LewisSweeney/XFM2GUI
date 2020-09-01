package serial.notXFM2;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.github.steadiestllama.xfm2gui.serial.SerialHandlerBridge;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotXFM2WriteToXFM {
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
        } else if (os.contains("win")) {
            com.fazecast.jSerialComm.SerialPort[] jSerialComms = com.fazecast.jSerialComm.SerialPort.getCommPorts();
            for (com.fazecast.jSerialComm.SerialPort port : jSerialComms) {
                if (port.getSystemPortName().equals("COM3")) {
                    jSerialCommPort = port;
                }
            }
            serialHandlerBridge.setSerialPort(jSerialCommPort);
            assertNotNull(jSerialCommPort);
        } else {
            // Again, direct reference, although more likely to be correct if only one device is connected...
            jsscSerialPort = new SerialPort("/dev/ttyACM0");
            serialHandlerBridge.setSerialPort(jsscSerialPort);
            assertNotNull(jsscSerialPort);
        }
    }


    @Test
    public void SettingParameterValuesIndividuallyToRandomByteValuesShouldChangeNothing() throws IOException, SerialPortException, InterruptedException {

        int low = 0;
        int high = 255;

        serialHandlerBridge.readProgram(1);
        byte[] initData = serialHandlerBridge.getAllValues();

        for (int i = 0; i < 512; i++) {
            int gen = random.nextInt(high - low) + low;
            serialHandlerBridge.setIndividualValue(i, gen);
        }

        byte[] postWriteData = serialHandlerBridge.getAllValues();

        for (int i = 0; i < initData.length; i++) {
            assertEquals(initData[i], (int) postWriteData[i] & 0xff, "Values should be equal due to no connected device");
        }

    }

    @Test
    public void SettingParameterValuesAllInOneGoToRandomByteValuesWillChangeNothing() throws IOException, SerialPortException {

        serialHandlerBridge.readProgram(1);
        byte[] initData = serialHandlerBridge.getAllValues();

        byte[] generatedData = new byte[512];
        random.nextBytes(generatedData);

        serialHandlerBridge.setAllValues(generatedData);

        byte[] postWriteData = serialHandlerBridge.getAllValues();

        assertArrayEquals(initData, postWriteData, "Values should be equal due to no connected device");


    }


}
