package control.wave;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.aber.lsweeney.controls.ParameterControl;
import uk.ac.aber.lsweeney.layouts.ControlLayoutFactory;

import static org.junit.jupiter.api.Assertions.*;


public class TestWave {

    ControlLayoutFactory controlLayoutFactory = ControlLayoutFactory.getSingleInstance();

    @BeforeAll
    public static void initialise(){
        // NEEDED FOR INITIALIZING TOOLKIT
      Platform.startup(() -> {

      });
    }

    @Test
    public void ChangingValueOfComboBoxChangesImageviewContentsAndParamFieldValue(){
        String testString = "Algo:2:WAVE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);
        Image tempImage;

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        assertEquals(0,tester.getWaves().getSelectionModel().getSelectedIndex(), "Initially selected index of Waves should be 0");

        tempImage = tester.getWaveImage().getImage();

        tester.getWaves().getSelectionModel().select(2);

        assertEquals(2,tester.getWaves().getSelectionModel().getSelectedIndex(), "Index of waves should now be 2");
        assertEquals(2, tester.getParamField().getValue(), "Value of ParamField should now be 2");
        assertNotEquals(tempImage,tester.getWaveImage().getImage(),"Images should not match");

    }

    @Test
    public void ChangingValueOfParamFieldChangesImageviewContentsAndComboBoxValue(){
        String testString = "Algo:2:WAVE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);
        Image tempImage = tester.getWaveImage().getImage();

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        assertEquals(0,tester.getWaves().getSelectionModel().getSelectedIndex(), "Initially selected index of Waves should be 0");

        assertNotNull(tempImage);

        tester.getParamField().setValue(2);

        assertEquals(2,tester.getWaves().getSelectionModel().getSelectedIndex(), "Index of waves should now be 2");
        assertEquals(2, tester.getParamField().getValue(), "Value of ParamField should now be 2");
        assertNotEquals(tempImage,tester.getWaveImage().getImage(),"Images should not match");

    }

    @Test
    public void ChangingValueOfParamFieldToUnderMinValSetsMinIndexOnComboboxAndImage(){

        String testString = "Algo:2:WAVE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        tester.getWaves().getSelectionModel().select(1);
        Image tempImage = tester.getWaveImage().getImage();

        assertEquals(1, tester.getParamField().getValue(), "Initial value of ParamField should be 1");
        assertEquals(1,tester.getWaves().getSelectionModel().getSelectedIndex(), "Initially selected index of Waves should be 1");

        assertNotNull(tempImage);

        tester.getParamField().setValue(-255);

        assertEquals(0,tester.getWaves().getSelectionModel().getSelectedIndex(), "Index of waves should now be 0");
        assertEquals(0, tester.getParamField().getValue(), "Value of ParamField should now be 0");
        assertNotEquals(tempImage,tester.getWaveImage().getImage(),"Images should not match");

    }

    @Test
    public void ChangingIndexOfComboboxToUnderMaxValChangesNothing(){

        String testString = "Algo:2:WAVE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);

        tester.getWaves().getSelectionModel().select(1);
        Image tempImage = tester.getWaveImage().getImage();

        assertEquals(1, tester.getParamField().getValue(), "Initial value of ParamField should be 1");
        assertEquals(1,tester.getWaves().getSelectionModel().getSelectedIndex(), "Initially selected index of Waves should be 1");
        assertNotNull(tempImage);

        tester.getWaves().getSelectionModel().select(-255);

        assertEquals(1,tester.getWaves().getSelectionModel().getSelectedIndex(), "Index of waves should still be 1");
        assertEquals(1, tester.getParamField().getValue(), "Value of ParamField should still be 1");
        assertEquals(tempImage,tester.getWaveImage().getImage(),"Images should match");

    }

    @Test
    public void ChangingValueOfParamFieldToOverMaxValSetsMaxIndexOnComboboxAndImage(){

        String testString = "Algo:2:WAVE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);
        Image tempImage = tester.getWaveImage().getImage();

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        assertEquals(0,tester.getWaves().getSelectionModel().getSelectedIndex(), "Initially selected index of Waves should be 0");

        assertNotNull(tempImage);

        tester.getParamField().setValue(255);

        assertEquals(7,tester.getWaves().getSelectionModel().getSelectedIndex(), "Index of waves should now be 7");
        assertEquals(7, tester.getParamField().getValue(), "Value of ParamField should now be 7");
        assertNotEquals(tempImage,tester.getWaveImage().getImage(),"Images should not match");

    }

    @Test
    public void ChangingIndexOfComboboxToOverMaxValChangesNothing(){

        String testString = "Algo:2:WAVE";
        ParameterControl tester = controlLayoutFactory.createControl(testString);
        Image tempImage = tester.getWaveImage().getImage();

        assertEquals(0, tester.getParamField().getValue(), "Initial value of ParamField should be 0");
        assertEquals(0,tester.getWaves().getSelectionModel().getSelectedIndex(), "Initially selected index of Waves should be 0");
        assertNotNull(tempImage);

        tester.getWaves().getSelectionModel().select(255);

        assertEquals(0,tester.getWaves().getSelectionModel().getSelectedIndex(), "Index of waves should still be 0");
        assertEquals(0, tester.getParamField().getValue(), "Value of ParamField should still be 0");
        assertEquals(tempImage,tester.getWaveImage().getImage(),"Images should not match");

    }
}
