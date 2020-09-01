package serial.disconnected;

import jssc.SerialPortException;
import org.junit.jupiter.api.Test;
import com.github.steadiestllama.xfm2gui.serial.SerialHandlerBridge;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestDisconnectReadFromXFM {
    static SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

    @Test
    public void ReadingWithNoDeviceResultsInOneLengthDataArray() throws IOException, SerialPortException {
        byte[] data = serialHandlerBridge.getAllValues();
        assertEquals(data.length,1,"Serial Handler will return a 1 length array when nothing is connected");
    }
}
