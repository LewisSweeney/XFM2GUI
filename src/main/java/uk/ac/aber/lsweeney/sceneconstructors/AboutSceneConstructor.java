package uk.ac.aber.lsweeney.sceneconstructors;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Creates the scene for the About section of the program
 */
public class AboutSceneConstructor {
    Stage newStage = new Stage();
    String style = this.getClass().getResource("/stylesheets/style.css").toExternalForm();
    public AboutSceneConstructor(){
        Scene scene = sceneConstruction();
        scene.getStylesheets().add(style);
        newStage.setScene(scene);
        newStage.setAlwaysOnTop(true);
    }

    private Scene sceneConstruction(){

        VBox box = new VBox(getAboutSection(),getThirdPartySection());

        return new Scene(box);
    }

    private VBox getAboutSection(){
        VBox about = new VBox();
        Label aboutTitle = new Label("About This App");
        aboutTitle.getStyleClass().add("about-section-title");
        Label aboutText = new Label("This application has been developed as part of my MSc dissertation. The app is intended for use with the XFM2 synthesizer module created by futur3soundz. " +
                "The aim of the project was to create a platform independent application to program the device using its USB-over-uk.ac.aber.les35.serial capabilities.\n" +
                "\n" +
                "The application can change all of the parameters and patches of your XFM2 device, as well as save and load programs locally.\n" +
                "Unfortunately, the device will still need access to a windows machine to install the initial program.\n" +
                "\n" +
                "If you're seeing this text, I'd like to thank you for downloading this app and I hope it's been useful to you");
        about.getChildren().addAll(aboutTitle,aboutText);
        aboutText.getStyleClass().add("about-text");
        about.getStyleClass().add("about");
        return about;
    }

    /*
    private VBox getContactSection(){
        VBox about = new VBox();
        Label aboutTitle = new Label("About This App");
        aboutTitle.getStyleClass().add("about-section-title");
        Label aboutText = new Label("This application has been developed as part of my MSc dissertation. The app is intended for use with the XFM2 synthesizer module created by futur3soundz. " +
                "The aim of the project was to create a platform independent application to program the device using its USB-over-uk.ac.aber.les35.serial capabilities.\n" +
                "\n" +
                "The application can change all of the parameters and patches of your XFM2 device, as well as save and load programs locally.\n" +
                "Unfortunately, the device will still need access to a windows machine to install the initial program.\n" +
                "\n" +
                "If you're seeing this text, I'd like to thank you for downloading this app and I hope it's been useful to you");
        about.getChildren().addAll(aboutTitle,aboutText);
        aboutText.getStyleClass().add("about-text");
        about.getStyleClass().add("about");
        return about;
    }
    */

    private VBox getThirdPartySection(){
        VBox box = new VBox();
        Label aboutTitle = new Label("Third-Party Code");
        Label aboutText = new Label("This app wouldn't have been possible without the use of various 3rd-party library and code snippets. " +
                "Below is a list of these 3rd-party pieces and links to where to find them.\n\n");

        Label jsscTitle = new Label("JSSC");
        Label jsscText = new Label("Used for uk.ac.aber.les35.serial communication. Helped keep uk.ac.aber.les35.serial comms consistent across platforms.");
        Hyperlink jsscLink = new Hyperlink("Find it on GitHub");

        javafx.event.EventHandler<? super MouseEvent> jsscEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                Desktop.getDesktop().browse(new URL("https://github.com/scream3r/java-simple-serial-connector").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        };
        jsscLink.setOnMouseClicked(jsscEventHandler);

        Label xLoadTitle = new Label("XLoad");
        Label xLoadText = new Label("The program found on the futur3soundz website that is used to initialise the device.");
        Hyperlink xLoadLink = new Hyperlink("Find it on the futur3soundz website");

        javafx.event.EventHandler<? super MouseEvent> xLoadEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                Desktop.getDesktop().browse(new URL("https://www.futur3soundz.com/").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        };
        xLoadLink.setOnMouseClicked(xLoadEventHandler);

        Label intTitle = new Label("IntFields");
        Label intText = new Label("An adaption of the TextField class the takes only numbers in a given range. Useful for the parameter layout.");
        Hyperlink intLink = new Hyperlink("Find it on StackOverflow");

        javafx.event.EventHandler<? super MouseEvent> intEventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                Desktop.getDesktop().browse(new URL("https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx/18959399").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        };
        intLink.setOnMouseClicked(intEventHandler);

        box.getChildren().addAll(aboutTitle,aboutText,jsscTitle,jsscText,jsscLink,xLoadTitle,xLoadText,xLoadLink,intTitle,intText,intLink);

        aboutText.getStyleClass().add("about-text");
        jsscText.getStyleClass().add("about-text");
        xLoadText.getStyleClass().add("about-text");
        intText.getStyleClass().add("about-text");

        aboutTitle.getStyleClass().add("about-section-title");
        jsscTitle.getStyleClass().add("about-section-subtitle");
        xLoadTitle.getStyleClass().add("about-section-subtitle");
        intTitle.getStyleClass().add("about-section-subtitle");

        jsscLink.getStyleClass().add("about-link");
        xLoadLink.getStyleClass().add("about-link");

        box.getStyleClass().add("about");
        return box;
    }

    public void showStage(){
        newStage.showAndWait();
    }
}
