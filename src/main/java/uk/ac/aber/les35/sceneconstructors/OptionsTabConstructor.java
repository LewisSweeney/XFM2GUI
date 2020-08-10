package uk.ac.aber.les35.sceneconstructors;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OptionsTabConstructor {
    Stage stage;
    Scene scene;
    VBox testOption = new VBox();
    CheckBox darkMode;

    String style = this.getClass().getResource("/stylesheets/style.css").toExternalForm();
    String dark =  this.getClass().getResource("/stylesheets/darkmode.css").toExternalForm();

    public OptionsTabConstructor(Stage stage, Scene scene){
        this.scene = scene;
        this.stage = stage;
        darkMode = new CheckBox("Dark Mode");
        darkMode.setOnAction(actionEvent -> onDarkModeToggle());
        testOption.getChildren().add(darkMode);
    }

    public VBox getTestOption(){
        return testOption;
    }

    private void onDarkModeToggle(){
        scene.getStylesheets().removeAll();
        if(darkMode.isSelected()){
            scene.getStylesheets().addAll(style,dark);
        } else{
            scene.getStylesheets().add(style);
        }
        stage.setScene(scene);
    }
}
