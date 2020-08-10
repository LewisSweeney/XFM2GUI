package uk.ac.aber.les35.functionhandlers;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import uk.ac.aber.les35.enums.ALERT_TYPE;

public class AlertHandler {
    Stage stage;
    Scene scene;

    public void SendAlert(ALERT_TYPE alert_type){
        switch(alert_type){
            case NOT_XFM -> ShowAlert(Alert.AlertType.WARNING,"Selected Device is not an XFM2", null, "The selected device is not compatible with this application.\n\nPlease select another device.");
            case NO_DEVICE -> ShowAlert(Alert.AlertType.WARNING,"No Serial Devices", null, "There are no serial devices available.\n\nYou may still edit and save patches locally, but any changes made cannot be applied to your XFM2.\n\nPlease ensure your device is plugged in and restart this program.");
            case NO_PATCH_CHOSEN -> ShowAlert(Alert.AlertType.WARNING,"No Patch Chosen", null, "No patch number selected.\nPlease select a patch number if you'd like to save your patch.\n\n(You may want to save your current program locally first)");
        }
    }

    private void ShowAlert(Alert.AlertType alertType, String title, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
