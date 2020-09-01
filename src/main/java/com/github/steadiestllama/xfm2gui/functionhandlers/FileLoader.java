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
import com.github.steadiestllama.xfm2gui.enums.FILE_TYPE;

import java.io.*;
import java.util.ArrayList;

/**
 * Class used for loading patches from local storage.
 * User will choose the filepath and name, and the program will ensure it's in the proprietary XFM2 format
 *
 * If the file exists the method loadXFM2FromFile method will read each line and produce an arraylist of the strings gathered.
 * Code modified from https://www.genuinecoder.com/save-files-javafx-filechooser/
 */
public class FileLoader {


    public ArrayList<String> loadXFM2FromFile(Stage loadStage) {
        ArrayList<String> lines = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XFM2 files (*.xfm2)", "*.xfm2");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(loadStage);


        if (file != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String line = reader.readLine();
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lines;
    }

    public File loadConfigFile(Stage loadStage, FILE_TYPE file_type){
        FileChooser fileChooser = new FileChooser();

        String[] fileChooserStrings = new String[2];

        switch (file_type){
            case TUNING -> {
                fileChooserStrings[0] = "XFM2 Tuning Files (*.bin)";
                fileChooserStrings[1] = "*.bin";
            }
            case BANK -> {
                fileChooserStrings[0] = "XFM2 Bank (*.bank)";
                fileChooserStrings[1] = "*.bank";
            }
        }

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(fileChooserStrings[0], fileChooserStrings[1]);
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(loadStage);


        if (file != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String line = reader.readLine();
                while (line != null) {
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}
