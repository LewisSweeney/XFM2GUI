package control.bitwise;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.controls.ParameterControl;
import uk.ac.aber.lsweeney.layouts.ControlLayoutFactory;

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
        assertFalse(tester.getBitwiseCheckboxes()[0].isSelected(), "Box 0 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[1].isSelected(), "Box 1 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[2].isSelected(), "Box 2 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[3].isSelected(), "Box 3 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[4].isSelected(), "Box 4 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[5].isSelected(), "Box 5 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[6].isSelected(), "Box 6 should not be ticked");
        tester.getParamField().setValue(3);
        assertEquals(3,tester.getParamField().getValue(), "ParamField val should now be 3");
        assertFalse(tester.getBitwiseCheckboxes()[0].isSelected(), "Box 0 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[1].isSelected(), "Box 1 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[2].isSelected(), "Box 2 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[3].isSelected(), "Box 3 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[4].isSelected(), "Box 4 should not be ticked");
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
        assertFalse(tester.getBitwiseCheckboxes()[0].isSelected(), "Box 0 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[1].isSelected(), "Box 1 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[2].isSelected(), "Box 2 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[3].isSelected(), "Box 3 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[4].isSelected(), "Box 4 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[5].isSelected(), "Box 5 should not be ticked");
        assertFalse(tester.getBitwiseCheckboxes()[6].isSelected(), "Box 6 should not be ticked");
        tester.getParamField().setValue(127);
        assertEquals(127,tester.getParamField().getValue(), "Paramfield val should now be 128");
        assertTrue(tester.getBitwiseCheckboxes()[0].isSelected(), "Box 0 should now be ticked");
        assertTrue(tester.getBitwiseCheckboxes()[1].isSelected(), "Box 1 should now be ticked");
        assertTrue(tester.getBitwiseCheckboxes()[2].isSelected(), "Box 2 should now be ticked");
        assertTrue(tester.getBitwiseCheckboxes()[3].isSelected(), "Box 3 should now be ticked");
        assertTrue(tester.getBitwiseCheckboxes()[4].isSelected(), "Box 4 should now be ticked");
        assertTrue(tester.getBitwiseCheckboxes()[5].isSelected(), "Box 5 should now be ticked");
        assertTrue(tester.getBitwiseCheckboxes()[6].isSelected(), "Box 6 should now be ticked");
    }
}
