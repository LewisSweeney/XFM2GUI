package serial.JSSC;

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

    static SerialPort serialPort;
    static SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

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
    public void readingXFM2IntoEmptyByteArrayWillGet512BytesOfData() throws IOException, SerialPortException {
        byte[] data = serialHandlerBridge.getAllValues();
        assertEquals(data.length,512,"Data received from connected device should be 512 bytes long");
    }
}
