package uk.ac.aber.lsweeney.sceneconstructors;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

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
        newStage.initStyle(StageStyle.UTILITY);
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
                "The aim of the project was to create a platform independent application to program the device using its USB-over-serial capabilities.\n" +
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

    private VBox getThirdPartySection(){
        BufferedReader bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/data/parameters/thirdparty.txt")));
        ArrayList<String> fileStrings = new ArrayList<>();
        ArrayList<VBox> layouts = new ArrayList<>();
        int sectionLength = 4;

        try{
            while (bReader.ready()){
                fileStrings.add(bReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0;i < fileStrings.size();i += sectionLength){
            String[] newItem = new String[sectionLength];
            newItem[0] = fileStrings.get(i);
            newItem[1] = fileStrings.get(i+1);
            newItem[2] = fileStrings.get(i+2);
            newItem[3] = fileStrings.get(i+3);

            layouts.add(getReferenceSection(newItem));
        }

        VBox box = new VBox();
        Label thirdPartyTitle = new Label("Third-Party Code");
        Label thirdPartyText = new Label("This app wouldn't have been possible without the use of various 3rd-party library and code snippets. " +
                "Below is a list of these 3rd-party pieces and links to where to find them.\n\n");


        GridPane gridPane = new GridPane();
        GridPane.setConstraints(layouts.get(0),0,0);
        GridPane.setConstraints(layouts.get(1),0,1);
        GridPane.setConstraints(layouts.get(2),0,2);
        GridPane.setConstraints(layouts.get(3), 1,0);
        GridPane.setConstraints(layouts.get(4),1,1);
        GridPane.setConstraints(layouts.get(5),1,2);
        gridPane.getChildren().addAll(layouts);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        thirdPartyText.getStyleClass().add("about-text");

        thirdPartyTitle.getStyleClass().add("about-section-title");

        box.getStyleClass().add("about");
        box.getChildren().addAll(thirdPartyTitle,thirdPartyText,gridPane);
        return box;
    }

    private VBox getReferenceSection(String[] items){
        Label title = new Label(items[0]);
        Label text = new Label(items[1]);
        Hyperlink link = new Hyperlink(items[2]);

        javafx.event.EventHandler<? super MouseEvent> eventHandler = (EventHandler<MouseEvent>) mouseEvent -> {
            try {
                Desktop.getDesktop().browse(new URL(items[4]).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        };
        link.setOnMouseClicked(eventHandler);

        text.getStyleClass().add("about-text");
        title.getStyleClass().add("about-section-title");
        link.getStyleClass().add("about-link");

        return new VBox(title,text,link);
    }

    public void showStage(){
        newStage.showAndWait();
    }
}
