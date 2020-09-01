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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Class used for saving patches to local storage.
 * User will choose the filepath and name, and the program will ensure it's in the proprietary XFM2 format
 */
public class PatchSaver {

    /**
     * Method that prompts the user to choose a file location and name for the newly saved file.
     * Should automatically scrub contents if file passed is an existing file.
     *
     * Save dialog is a JavaFX feature and should work correctly across platforms
     *
     * @param lines ArrayList of strings that hold key and value
     * @param saveStage JavaFX stage for displaying the Save dialog
     * Code modified from https://www.genuinecoder.com/save-files-javafx-filechooser/
     */
    public void saveToFile(ArrayList<String> lines, Stage saveStage) {

        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XFM2 files (*.xfm2)", "*.xfm2");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(saveStage);

        if (file != null) {
            try {
                PrintWriter writer = new PrintWriter(file);
                for (String l : lines) {
                    writer.println(l);
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
