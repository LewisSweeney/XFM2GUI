package uk.ac.aber.les35.functionhandlers;

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
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}
