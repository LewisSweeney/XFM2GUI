package uk.ac.aber.lsweeney.layouts;

import uk.ac.aber.lsweeney.controls.*;

/**
 * This class creates the layout for each individual parameter control
 * Creates different control depending on passed parameters, but always creates an IntField to store parameter values,
 * even if not visible
 */
public class ControlLayoutFactory {
    String[] paramSplit;

    ParameterControl control;

    private static final ControlLayoutFactory SINGLE_INSTANCE = new ControlLayoutFactory();

    public ParameterControl createControl(String p){
        paramSplit = p.split(":");

        if (paramSplit.length > 2) {
            switch (paramSplit[2]) {
                case "BIT" -> bitwiseControl(paramSplit);
                case "WAVE" -> waveControl();
                case "TOGGLE" -> switchControl();
                default -> sliderControl(paramSplit);
            }
        } else {
            sliderControl(paramSplit);
        }

        return control;
    }
    /**
     * Creates a control with a combobox and imageview to display the wave shape of the currently selected value
     */
    private void waveControl() {
        control = new WaveControl(paramSplit[0], paramSplit[1], 7);
    }

    /**
     * Creates a bitwise control with relevant numbers of checkboxes
     *
     * @param paramSplit The split up parameter string passed into the constructor
     */
    private void bitwiseControl(String[] paramSplit) {
        control = new BitwiseControl(paramSplit[0], paramSplit[1], 255, Integer.parseInt(paramSplit[3]), paramSplit[4]);
    }

    /**
     * Creates a control that has a slider and a visible IntField to control parameter values
     *
     * @param paramSplit The split up parameter string passed into the constructor
     */
    private void sliderControl(String[] paramSplit) {
        if (paramSplit.length > 2) {
            control = new SliderControl(paramSplit[0], paramSplit[1], Integer.parseInt(paramSplit[2]));
        } else {
            control = new SliderControl(paramSplit[0], paramSplit[1], 255);
        }
    }

    /**
     * Creates a control that has an on/off toggle with radio buttons
     */
    private void switchControl() {
        control = new ToggleControl(paramSplit[0], paramSplit[1], 1);
    }

    public static ControlLayoutFactory getSingleInstance(){
        return SINGLE_INSTANCE;
    }
}
