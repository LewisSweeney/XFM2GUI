package serial.JSSC;

import javafx.application.Platform;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestChangePatch {

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
    public void ReadingOneProgramThenAnotherResultsInDifferentParameterValues() throws IOException, SerialPortException {

        serialHandlerBridge.readProgram(1);
        byte[] initData = serialHandlerBridge.getAllValues();
        serialHandlerBridge.readProgram(12);
        byte[] newData = serialHandlerBridge.getAllValues();

        // Basically looking here that there is at least one difference between the initial state of the device and the 12th program
        // 12 is chosen arbitrarily, essentially needs to not be 1
        // This test will fail if all values are the same between programs, but there's no way to check which program is currently loaded in...
        boolean differenceBetweenTwoDataSets = false;
        int differenceIndex = -1;
        for(int i = 0;i < 512;i++){
            if(differenceBetweenTwoDataSets){
                break;
            } else{
                if(initData[i] != newData[i]){
                    differenceIndex = i;
                    differenceBetweenTwoDataSets = true;
                }
            }
        }

        assertTrue(differenceBetweenTwoDataSets, "There should be a difference between the two datasets");
        assertNotEquals(initData[differenceIndex],newData[differenceIndex], "These two vals are the first different ones!");
    }

    @Test
    public void SendingANumberThatIsOutOfPatchRangeResultsInNothingChanging() throws IOException, SerialPortException {
        serialHandlerBridge.readProgram(1);
        byte[] initData = serialHandlerBridge.getAllValues();
        serialHandlerBridge.readProgram(129);
        byte[] newData = serialHandlerBridge.getAllValues();

        for(int i = 0;i<512;i++){
            assertEquals(initData[i],newData[i],"Vals should be the same!");
        }
    }


}
