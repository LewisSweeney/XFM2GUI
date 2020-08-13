package uk.ac.aber.lsweeney.functionhandlers;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

/**
 * Class used for loading patches from local storage.
 * User will choose the filepath and name, and the program will ensure it's in the proprietary XFM2 format
 */
public class PatchLoader {

    /** Prompts a user to find a .XFM2 file to load from their local storage.
     * If the file exists the method will read each line and produce an arraylist of the strings gathered.
     *
     * Code adapted from:
     *
     * @param loadStage JavaFX stage for displaying Open dialog
     * @return Returns a list of lines from the saved file
     *
     * Code modified from https://www.genuinecoder.com/save-files-javafx-filechooser/
     */
    public ArrayList<String> loadFromFile(Stage loadStage) {
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
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        return lines;
    }

}
