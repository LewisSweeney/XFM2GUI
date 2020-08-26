package serial.JSSC;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestSavePatch {
    static String os = System.getProperty("os.name").toLowerCase();

    static SerialPort serialPort;
    static SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

    Random random = new Random();

    @BeforeAll
    public static void initialise(){

        if(os.contains("mac")){
            // Direct reference to the XFM2 device I am using - would need changing on another machine
            serialPort = new SerialPort("/dev/tty.usbserial-210328AD3A891");
        } else{
            // Again, direct reference, although more likely to be correct if only one device is connected...
            serialPort = new SerialPort("/dev/ttyUSB1");
        }
        serialHandlerBridge.setSerialPort(serialPort);

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

        assertNotEquals(tempData,programData);

        // Super minimal chance this will fail...
        for(int i = 0; i<512;i++){
            assertEquals(generatedData[i],programData[i], "Generated data should be equal to data from program 12");
        }

    }

}
