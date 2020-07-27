package main.java.utilities;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.externalcode.IntField;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PatchSaver {

    // Code for this method is adapted from https://www.genuinecoder.com/save-files-javafx-filechooser/
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
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}
