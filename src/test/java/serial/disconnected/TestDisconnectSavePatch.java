package serial.disconnected;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.jupiter.api.Test;
import com.github.steadiestllama.xfm2gui.serial.SerialHandlerBridge;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TestDisconnectSavePatch {
    static SerialPort serialPort;
    static SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

    Random random = new Random();

    @Test
    public void Saving512RandomValuesToNonConnectedDeviceShouldResultInNoChange() throws IOException, SerialPortException {

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

        assertArrayEquals(tempData, programData, "Data should be equal as all calls should return -1");


    }

}
