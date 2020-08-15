package control.toggle;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.controls.ParameterControl;
import uk.ac.aber.lsweeney.layouts.ControlLayoutFactory;

import static org.junit.jupiter.api.Assertions.*;

public class TestToggle {

    ControlLayoutFactory controlLayoutFactory = ControlLayoutFactory.getSingleInstance();

    @BeforeAll
    public static void initialise() {
        // NEEDED FOR INITIALIZING TOOLKIT
        Platform.startup(() -> {

        });
    }

    @Test
    public void FiringRadioButtonOneThenTwoLeavesOnlyTwoEnabled() {
        String testString = "Algo:2:TOGGLE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        tester.getRadioButtons()[0].fire();

        assertTrue(tester.getRadioButtons()[0].isSelected(), "Radio Button One should be selected");
        assertFalse(tester.getRadioButtons()[1].isSelected(), "Radio Button Two should not be selected");

        tester.getRadioButtons()[1].fire();

        assertFalse(tester.getRadioButtons()[0].isSelected(), "Radio Button One should not be selected");
        assertTrue(tester.getRadioButtons()[1].isSelected(), "Radio Button Two should be selected");
    }

    @Test
    public void FiringRadioButtonOneSetsParamfieldValueTo0() {
        String testString = "Algo:2:TOGGLE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");

        tester.getRadioButtons()[0].fire();

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should still be 0");
    }

    @Test
    public void FiringRadioButtonTwoSetsParamfieldValueTo1() {
        String testString = "Algo:2:TOGGLE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");

        tester.getRadioButtons()[1].fire();

        assertEquals(1, tester.getParamField().getValue(), "Initial value of ParamField should now be 1");
    }

    @Test
    public void SettingParamfieldValueTo0SetsRadioButtonOneAsSelected(){
        String testString = "Algo:2:TOGGLE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        tester.getRadioButtons()[1].fire();

        assertEquals(1, tester.getParamField().getValue(), "Initial value of ParamField should be 1");
        assertFalse(tester.getRadioButtons()[0].isSelected(), "RadioButton one should not be selected");

        tester.getParamField().setValue(0);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should now be 0");
        assertTrue(tester.getRadioButtons()[0].isSelected(), "RadioButton one should be selected");
    }

    @Test
    public void SettingParamfieldValueTo1SetsRadioButtonTwoAsSelected(){
        String testString = "Algo:2:TOGGLE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);


        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 1");
        assertFalse(tester.getRadioButtons()[1].isSelected(), "Radio Button two should not be selected");

        tester.getParamField().setValue(1);

        assertEquals(1, tester.getParamField().getValue(), "Initial value of ParamField should now be 0");
        assertTrue(tester.getRadioButtons()[1].isSelected(), "Radio Button two should now be selected");
    }

    @Test
    public void SettingParamfieldValueToMoreThan1SetsRadioButtonTwoAsSelected(){
        String testString = "Algo:2:TOGGLE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        assertFalse(tester.getRadioButtons()[1].isSelected(), "Radio Button two should not be selected");

        tester.getParamField().setValue(255);

        assertEquals(1, tester.getParamField().getValue(), "Initial value of ParamField should now be 1");
        assertTrue(tester.getRadioButtons()[1].isSelected(), "Radio Button two should now be selected");
    }

    @Test
    public void SettingParamfieldValueToLessThan0SetsRadioButtonOneAsSelected(){
        String testString = "Algo:2:TOGGLE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        tester.getRadioButtons()[1].fire();
        assertEquals(1, tester.getParamField().getValue(), "Initial value of ParamField should be 1");
        assertFalse(tester.getRadioButtons()[0].isSelected(), "Radio Button one should not be selected");

        tester.getParamField().setValue(-255);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should now be 0");
        assertTrue(tester.getRadioButtons()[0].isSelected(), "Radio Button one should now be selected");
    }
}
