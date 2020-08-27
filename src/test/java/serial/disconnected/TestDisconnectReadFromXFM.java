package serial.disconnected;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestDisconnectReadFromXFM {
    static SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

    @Test
    public void readingWithNoDeviceResultsInOneLengthDataArray() throws IOException, SerialPortException {
        byte[] data = serialHandlerBridge.getAllValues();
        assertEquals(data.length,1,"Serial Handler will return a 1 length array when nothing is connected");
    }
}
