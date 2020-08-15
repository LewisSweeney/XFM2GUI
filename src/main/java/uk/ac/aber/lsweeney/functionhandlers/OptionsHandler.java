package uk.ac.aber.lsweeney.functionhandlers;

import uk.ac.aber.lsweeney.enums.MIDI_CHANNEL;
import uk.ac.aber.lsweeney.enums.OPERATION_MODE;

public class OptionsHandler {

    private boolean liveChanges;
    private boolean previousLiveChanges;
    private boolean midiLayering;

    private OPERATION_MODE opMode;

    private MIDI_CHANNEL unitZeroChannel;
    private MIDI_CHANNEL unitOneChannel;

    private static final OptionsHandler SINGLE_INSTANCE = new OptionsHandler();

    public static OptionsHandler getSingleInstance(){
        return SINGLE_INSTANCE;
    }

    public void setLiveChanges(boolean liveChanges){
        previousLiveChanges = this.liveChanges;
        this.liveChanges = liveChanges;

    }

    public void revertLiveChanges(){
        this.liveChanges = previousLiveChanges;
    }

    public boolean getLiveChanges(){
        return liveChanges;
    }



}
