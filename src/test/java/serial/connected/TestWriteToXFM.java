package serial.connected;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TestWriteToXFM {
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
    public void SettingParameterValuesIndividuallyToRandomByteValuesThenReadingItWillGetSetValues() throws IOException, SerialPortException, InterruptedException {

        int[] generatedData = new int[512];
        int low = 0;
        int high = 255;

        serialHandlerBridge.readProgram(1);
        byte[] initData = serialHandlerBridge.getAllValues();

        for (int i = 0; i < 512; i++) {
            int gen = random.nextInt(high - low) + low;
            generatedData[i] = gen;
            serialHandlerBridge.setIndividualValue(i, gen);
        }

        byte[] postWriteData = serialHandlerBridge.getAllValues();
        System.out.println("IND WRITE DATA");
        for (int i = 0; i < 512; i++) {
                System.out.println(i + " " + generatedData[i] + " " + (postWriteData[i] & 0xff));
                assertEquals(generatedData[i], (int) postWriteData[i] & 0xff, "Values should be equal due to being set individually");
        }

        serialHandlerBridge.setAllValues(initData);

    }

    @Test
    public void SettingParameterValuesAllInOneGoToRandomByteValuesThenReadingItWillGetSetValues() throws IOException, SerialPortException {

        serialHandlerBridge.readProgram(1);
        byte[] initData = serialHandlerBridge.getAllValues();

        byte[] generatedData = new byte[512];
        random.nextBytes(generatedData);

        serialHandlerBridge.setAllValues(generatedData);

        byte[] postWriteData = serialHandlerBridge.getAllValues();

        System.out.println("MASS WRITE DATA");
        for (int i = 0; i < 512; i++) {
            System.out.println(i + " " + generatedData[i] + " " + postWriteData[i]);
            assertEquals(generatedData[i], postWriteData[i], "Values should be equal due to being set in one go");
        }

    }

    @Test
    public void SendingLessThan512BytesToBoardChangesNothing() throws IOException, SerialPortException {
        serialHandlerBridge.readProgram(1);
        byte[] initData = serialHandlerBridge.getAllValues();

        byte[] generatedData = new byte[511];
        random.nextBytes(generatedData);

        serialHandlerBridge.setAllValues(generatedData);

        byte[] postWriteData = serialHandlerBridge.getAllValues();
        boolean differenceFound = false;
        for (int i = 0; i < 511; i++) {
            if (generatedData[i] != postWriteData[i]) {
                differenceFound = true;
                break;
            }
        }

        assertTrue(differenceFound,"There should be at least one difference between the test arrays");

    }

    @Test
    public void SendingMoreThan512BytesToBoardChangesNothing() throws IOException, SerialPortException {
        serialHandlerBridge.readProgram(1);
        byte[] initData = serialHandlerBridge.getAllValues();

        byte[] generatedData = new byte[513];
        random.nextBytes(generatedData);

        serialHandlerBridge.setAllValues(generatedData);

        byte[] postWriteData = serialHandlerBridge.getAllValues();
        boolean differenceFound = false;
        for (int i = 0; i < 512; i++) {
            if (generatedData[i] != postWriteData[i]) {
                differenceFound = true;
                break;
            }
        }

        assertTrue(differenceFound,"There should be at least one difference between the test arrays");

    }


}
