package control.slider;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.controls.ParameterControl;
import uk.ac.aber.lsweeney.layouts.ControlLayoutFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSlider {

    ControlLayoutFactory controlLayoutFactory = ControlLayoutFactory.getSingleInstance();

    @BeforeAll
    public static void initialise(){
        // NEEDED FOR INITIALIZING TOOLKIT
        Platform.startup(() -> {

        });
    }

    @Test
    public void SetSliderValTo255ShouldSetParamFieldValTo255(){
        String testString = "Tester:0";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        assertEquals(0, tester.getSlider().getValue(), "Initial value of Slider should be 0");
        tester.getSlider().setValue(255);
        assertEquals(255, tester.getSlider().getValue(), "Slider value should now be 255");
        assertEquals(255,tester.getParamField().getValue(), "ParamField val should now be 255");
    }

    @Test
    public void SetParamFieldValTo255ShouldSetSliderValTo255(){
        String testString = "Tester:0";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        assertEquals(0, tester.getSlider().getValue(), "Initial value of Slider should be 0");
        tester.getParamField().setValue(255);
        assertEquals(255,tester.getParamField().getValue(), "ParamField val should now be 255");
        assertEquals(255, tester.getSlider().getValue(), "Slider value should now be 255");
    }

    @Test
    public void SetParamFieldValMoreThan255RoundsDownTo255(){
        String testString = "Tester:0";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        tester.getParamField().setValue(1000);
        assertEquals(255, tester.getParamField().getValue(), "Value of ParamField should now be 255");
    }

    @Test
    public void SetSliderValMoreThan255RoundsDownTo255(){
        String testString = "Tester:0";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getSlider().getValue(), "Initial value of Slider should be 0");
        tester.getParamField().setValue(1000);
        assertEquals(255, tester.getSlider().getValue(), "Value of Slider should now be 255");
    }

    @Test
    public void SetParamFieldValLessThan0RoundsUpTo0(){
        String testString = "Tester:0";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        tester.getParamField().setValue(-1000);
        assertEquals(0, tester.getParamField().getValue(), "Value of ParamField should still be 0");
    }

    @Test
    public void SetSliderValLessThan0RoundsUpTo0(){
        String testString = "Tester:0";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getSlider().getValue(), "Initial value of Slider should be 0");
        tester.getParamField().setValue(-1000);
        assertEquals(0, tester.getSlider().getValue(), "Value of Slider should still be 0");
    }

    @Test
    public void SettingParamFieldWithMaxValOfLessThan255TopsOutAtWhenSetTo255(){
        String testString = "Tester:0:128";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getSlider().getValue(), "Initial value of Slider should be 0");
        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        tester.getParamField().setValue(1000);
        assertEquals(128, tester.getSlider().getMax(), "128?");
        assertEquals(128, tester.getParamField().getValue(), "Value of Paramfield should now be 128");
        assertEquals(128, tester.getSlider().getValue(), "Value of Slider should now be 128");
    }

    @Test
    public void SettingSliderWithMaxValOfLessThan255TopsOutAtWhenSetTo255(){
        String testString = "Tester:0:128";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        assertEquals(0, tester.getSlider().getValue(), "Initial value of Slider should be 0");
        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        tester.getSlider().setValue(1000);
        assertEquals(128, tester.getSlider().getMax(), "128?");
        assertEquals(128, tester.getParamField().getValue(), "Value of Paramfield should now be 128");
        assertEquals(128, tester.getSlider().getValue(), "Value of Slider should now be 128");
    }
}

