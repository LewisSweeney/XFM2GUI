package uk.ac.aber.lsweeney.functionhandlers;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import uk.ac.aber.lsweeney.enums.ALERT_TYPE;

/**
 * Handles any alert windows that must be opened by the program, whether for errors or warnings
 */
public class AlertHandler {
    Stage stage;
    Scene scene;

    /**
     * Constructor that takes alert_type to decide on what parameters to send to the ShowAlert() method
     * @param alert_type Enum that determines alert content
     */
    public void SendAlert(ALERT_TYPE alert_type){
        switch(alert_type){
            case NOT_XFM -> ShowAlert(Alert.AlertType.WARNING,"No XFM2 device", "There doesn't seem to be an XFM2 compatible device connected.\n\nPlease ensure that your XFM2 is connected and not in MidiScope mode.");
            case NO_DEVICE -> ShowAlert(Alert.AlertType.WARNING,"No Serial Devices", "There are no serial devices available.\n\nYou may still edit and save patches locally, but any changes made cannot be applied to your XFM2.\n\nPlease ensure your device is plugged in and restart this program.");
            case NO_PATCH_CHOSEN -> ShowAlert(Alert.AlertType.WARNING,"No Patch Chosen", "No patch number selected.\nPlease select a patch number if you'd like to save your patch.\n\n(You may want to save your current program locally first)");
            case LINUX -> ShowAlert(Alert.AlertType.INFORMATION,"Linux Serial Permissions","The app can't seem to find any serial devices.\nIf you're on Linux you may not have the necessary privileges to communicate with serial devices.\n\n");

        }
    }

    /**
     * Constructs then displays an alert using the given parameters
     * @param alertType JavaFX enum that determines the decoration on the alert stage
     * @param title Title of the alert, displayed in control bar
     * @param contentText Main body of the alert text, contains the information the user needs to see
     */
    private void ShowAlert(Alert.AlertType alertType, String title, String contentText){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
