package com.github.steadiestllama.xfm2gui.functionhandlers;

/*

This file is part of XFM2GUI

Copyright 2020 Lewis Sweeney

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
USE OR OTHER DEALINGS IN THE SOFTWARE.

 */
import com.github.steadiestllama.xfm2gui.serial.SerialHandlerBridge;

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
