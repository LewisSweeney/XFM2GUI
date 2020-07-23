package main.java;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import jssc.SerialPort;
import jssc.SerialPortList;

import main.java.tabconstructors.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main extends Application {

    private static final int BOTTOM_BUTTON_WIDTH = 200;
    private ArrayList<Tab> tabs;
    private BorderPane border;
    private final String[] portNames = SerialPortList.getPortNames();
    SerialPort serialPort;
    private final ComboBox<String> serialPortPicker = new ComboBox<>();

    @Override
    public void start(Stage primaryStage) {

        initBorderPane();
        String style = this.getClass().getResource("/stylesheets/style.css").toExternalForm();
        Scene scene = new Scene(border);
        scene.getStylesheets().add(style);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("XFM2 GUI");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Initialises the BorderPane used as the main node for the GUI
    private void initBorderPane() {

        Label title = new Label("XFM2 GUI");
        title.setStyle("-fx-font: 72 arial;");

        border = new BorderPane();
        TabPane tabPane = new TabPane();


        initTabList();
        initTabs();
        VBox buttonBox = initBottomButtons();

        tabPane.setTabMinWidth(80);
        tabPane.setTabMaxWidth(80);
        tabPane.getTabs().addAll(tabs);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        VBox top = new VBox(title,serialPortPicker);
        top.setAlignment(Pos.CENTER);

        top.setStyle("-fx-padding: 0 0 10 0;");

        border.setTop(top);
        border.setCenter(tabPane);
        border.setBottom(buttonBox);
        border.setMaxWidth(1100);
        border.setMinWidth(1100);
        border.setMaxHeight((700));
        border.setMinHeight(700);

        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(tabPane, Pos.CENTER);
        BorderPane.setAlignment(buttonBox, Pos.CENTER);
    }

    /* Initialises the buttons at the bottom of the page
    TODO: Add button functionality
     */
    private VBox initBottomButtons() {
        VBox bottomButtons = new VBox();

        if(portNames.length > 0){
            serialPort = new SerialPort(portNames[0]);
            serialPortPicker.getItems().addAll(portNames);
        }
        else{
            serialPort = null;
            serialPortPicker.getItems().add("-NO PORTS AVAILABLE-");
        }


        serialPortPicker.getSelectionModel().selectFirst();
        serialPortPicker.setOnAction(e -> onSerialPortSelection());

        Button read = new Button("Read XFM2");
        Button write = new Button("Write to XFM2");
        Button setUnit0 = new Button("Set Unit 0");
        Button setUnit1 = new Button("Set Unit 1");
        Button saveCurrentPatch = new Button("Save Program");
        Button loadPatch = new Button("Load Program");

        serialPortPicker.setPrefWidth(BOTTOM_BUTTON_WIDTH * 1.5);

        HBox hBox = new HBox(read, setUnit0, saveCurrentPatch);
        HBox hBox2 = new HBox(write, setUnit1, loadPatch);

        hBox.getStyleClass().add("button-row");
        hBox2.getStyleClass().add("button-row");

        //hBox3.setAlignment(Pos.CENTER);


        bottomButtons.getChildren().addAll(hBox, hBox2);
        bottomButtons.getStyleClass().add("bottom-buttons");
        return bottomButtons;
    }

    // Reads the tablist.txt file and creates a tab for each line
    private void initTabList() {

        tabs = new ArrayList<>();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/parameters/tablist.txt")));
        try {
            String line = bReader.readLine();
            while (line != null) {
                Tab t = new Tab(line);
                tabs.add(t);
                line = bReader.readLine();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    // Initialises the previously created tabs with the relevant nodes/content
    private void initTabs() {
        OpTabConstructor opOneCon = new OpTabConstructor(OperatorNumber.ONE);
        OpTabConstructor opTwoCon = new OpTabConstructor(OperatorNumber.TWO);
        OpTabConstructor opThreeCon = new OpTabConstructor(OperatorNumber.THREE);
        OpTabConstructor opFourCon = new OpTabConstructor(OperatorNumber.FOUR);
        OpTabConstructor opFiveCon = new OpTabConstructor(OperatorNumber.FIVE);
        OpTabConstructor opSixCon = new OpTabConstructor(OperatorNumber.SIX);

        ProgramTabConstructor progTabCon = new ProgramTabConstructor();
        ModulationTabConstructor modTabCon = new ModulationTabConstructor();
        EffectsTabConstructor effTabCon = new EffectsTabConstructor();


        tabs.get(0).setContent(opOneCon.getLayout());
        tabs.get(1).setContent(opTwoCon.getLayout());
        tabs.get(2).setContent(opThreeCon.getLayout());
        tabs.get(3).setContent(opFourCon.getLayout());
        tabs.get(4).setContent(opFiveCon.getLayout());
        tabs.get(5).setContent(opSixCon.getLayout());
        tabs.get(6).setContent(progTabCon.getLayout());
        tabs.get(7).setContent(modTabCon.getLayout());
        tabs.get(8).setContent(effTabCon.getLayout());
    }

    // When the value of serialPortPicker changes, this method will change the active port to the one selected
    // If there are no available ports then does nothing.
    public void onSerialPortSelection(){
        if(portNames.length > 0){
            String portName = serialPortPicker.getValue();
            serialPort = new SerialPort(portName);
        }
    }
}
