package uk.ac.aber.lsweeney.functionhandlers;

import javafx.stage.Stage;
import jssc.SerialPortException;
import uk.ac.aber.lsweeney.enums.FILE_TYPE;
import uk.ac.aber.lsweeney.serial.SerialHandlerBridge;

import java.io.File;
import java.io.IOException;

public class OptionsHandler {

    private boolean liveChanges;
    private boolean previousLiveChanges;

    private final FileLoader fileLoader = new FileLoader();

    private final SerialHandlerBridge serialHandlerBridge = SerialHandlerBridge.getSINGLE_INSTANCE();

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

    /*
    UNUSED
    public void writeFileToDevice(FILE_TYPE file_type) throws IOException, SerialPortException {
        File file = fileLoader.loadConfigFile(new Stage(), file_type);

        switch (file_type){
            case BANK -> serialHandlerBridge.writeBank(file);
            case TUNING -> serialHandlerBridge.writeTunings(file);
        }

    }
     */

}
