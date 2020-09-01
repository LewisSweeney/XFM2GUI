package serial.disconnected;

import jssc.SerialPortException;
import org.junit.jupiter.api.Test;
import com.github.steadiestllama.xfm2gui.serial.SerialHandlerBridge;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestDisconnectChangePatch {

    static SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

    @Test
    public void ReadingProgramsWhenDisconnectedResultsInNoChange() throws IOException, SerialPortException {

        serialHandlerBridge.readProgram(1);
        byte[] initData = serialHandlerBridge.getAllValues();
        serialHandlerBridge.readProgram(12);
        byte[] newData = serialHandlerBridge.getAllValues();

        boolean differenceBetweenTwoDataSets = false;
        for(int i = 0;i < initData.length;i++){
            if(differenceBetweenTwoDataSets){
                break;
            } else{
                if(initData[i] != newData[i]){
                    differenceBetweenTwoDataSets = true;
                }
            }
        }

        assertFalse(differenceBetweenTwoDataSets, "There should be no difference between the datasets");
    }
}
