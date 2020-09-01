package control.bitwise;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.github.steadiestllama.xfm2gui.controls.ParameterControl;
import com.github.steadiestllama.xfm2gui.layouts.ControlLayoutFactory;

import static org.junit.jupiter.api.Assertions.*;

public class TestBitwiseSeven {
    ControlLayoutFactory controlLayoutFactory = ControlLayoutFactory.getSingleInstance();
    @BeforeAll
    public static void initialise(){
        // NEEDED FOR INITIALIZING TOOLKIT
        Platform.startup(() -> {

        });
    }

    @Test
    public void TickingTwoLeastSignificantBoxesShouldSetParamFieldValTo3(){
        String testString = "Algo:2:BIT:7:OP6/OP5/OP4/OP3/OP2/OP1/OUT";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        tester.getBitwiseCheckboxes()[6].fire();
        tester.getBitwiseCheckboxes()[5].fire();
        assertEquals(3,tester.getParamField().getValue(), "ParamField val should now be 3");
    }

    @Test
    public void SettingParamFieldValTo3ShouldOnlyTickTwoLeastSignificantCheckboxes(){
        String testString = "Algo:2:BIT:7:OP6/OP5/OP4/OP3/OP2/OP1/OUT";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        for(int i = 0;i<7;i++){
            assertFalse(tester.getBitwiseCheckboxes()[i].isSelected(), "Box " + i + " should not be ticked");
        }
        tester.getParamField().setValue(3);
        assertEquals(3,tester.getParamField().getValue(), "ParamField val should now be 3");
        for(int i = 0;i<5;i++){
            assertFalse(tester.getBitwiseCheckboxes()[i].isSelected(), "Box " + i + " should not be ticked");
        }
        assertTrue(tester.getBitwiseCheckboxes()[5].isSelected(), "Box 5 should now be ticked");
        assertTrue(tester.getBitwiseCheckboxes()[6].isSelected(), "Box 6 should now be ticked");
    }

    @Test
    public void SettingAllCheckBoxesTickedShouldEqual127(){
        String testString = "Algo:2:BIT:7:OP6/OP5/OP4/OP3/OP2/OP1/OUT";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(),"Paramfield val should be 0 intially");
        for(CheckBox c:tester.getBitwiseCheckboxes()){
            c.fire();
        }
        assertEquals(127,tester.getParamField().getValue(), "Paramfield val should now be 128");
    }

    @Test
    public void SettingParamFieldValTo127ShouldTickAllCheckBoxes(){
        String testString = "Algo:2:BIT:7:OP6/OP5/OP4/OP3/OP2/OP1/OUT";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(),"Paramfield val should be 0 intially");
        for(int i = 0;i<7;i++){
            assertFalse(tester.getBitwiseCheckboxes()[i].isSelected(), "Box " + i + " should not be ticked");
        }
        tester.getParamField().setValue(127);
        assertEquals(127,tester.getParamField().getValue(), "Paramfield val should now be 128");
        for(int i = 0;i<7;i++){
            assertTrue(tester.getBitwiseCheckboxes()[i].isSelected(), "Box " + i + " should be ticked");
        }
    }


}
