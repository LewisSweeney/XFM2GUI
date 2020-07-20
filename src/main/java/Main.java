package main.java;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.tabconstructors.EffectsTabConstructor;
import main.java.tabconstructors.Op1TabConstructor;
import main.java.tabconstructors.OpAltTabConstructor;
import main.java.tabconstructors.OperatorNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main extends Application {

    private static final int BOTTOM_BUTTON_WIDTH = 200;
    private ArrayList<Tab> tabs;
    private BorderPane border;

    @Override
    public void start(Stage primaryStage) throws Exception {

        initBorderPane();
        Scene scene = new Scene(border);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("XFM2 GUI");
        primaryStage.show();
    }

    private VBox bottomButtons() {
        VBox bottomButtons = new VBox();

        Button read = new Button("Read XFM2");
        Button write = new Button("Write to XFM2");
        Button setUnit0 = new Button("Set Unit 0");
        Button setUnit1 = new Button("Set Unit 1");
        Button saveCurrentPatch = new Button("Save Program");
        Button loadPatch = new Button("Load Program");

        read.setPrefWidth(BOTTOM_BUTTON_WIDTH);
        write.setPrefWidth(BOTTOM_BUTTON_WIDTH);
        setUnit0.setPrefWidth(BOTTOM_BUTTON_WIDTH);
        setUnit1.setPrefWidth(BOTTOM_BUTTON_WIDTH);
        saveCurrentPatch.setPrefWidth(BOTTOM_BUTTON_WIDTH);
        loadPatch.setPrefWidth(BOTTOM_BUTTON_WIDTH);

        HBox hBox = new HBox(read, setUnit0, saveCurrentPatch);
        HBox hBox2 = new HBox(write, setUnit1, loadPatch);

        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);

        hBox2.setAlignment(Pos.CENTER);
        hBox2.setSpacing(10);
        hBox2.setStyle("-fx-padding: 0 0 10 0");

        bottomButtons.getChildren().addAll(hBox, hBox2);
        bottomButtons.setSpacing(10);
        return bottomButtons;
    }

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

        return;
    }

    private void initBorderPane(){

        Label title = new Label("XFM2 GUI");
        title.setStyle("-fx-font: 72 arial;");

        border = new BorderPane();
        TabPane tabPane = new TabPane();


        initTabList();
        initTabs();
        VBox buttonBox = bottomButtons();

        tabPane.setTabMinWidth(80);
        tabPane.setTabMaxWidth(80);
        tabPane.getTabs().addAll(tabs);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        border.setTop(title);
        border.setCenter(tabPane);
        border.setBottom(buttonBox);
        border.setMaxWidth(1000);
        border.setMinWidth(1000);
        border.setMaxHeight((550));
        border.setMinHeight(550);

        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(tabPane, Pos.CENTER);
        BorderPane.setAlignment(buttonBox, Pos.CENTER);
    }

    private void initTabs(){
        Op1TabConstructor opOneCon = new Op1TabConstructor();
        OpAltTabConstructor opTwoCon = new OpAltTabConstructor(OperatorNumber.THREE);
        // OpAltTabConstructor opThreeCon = new OpAltTabConstructor(OperatorNumber.THREE);
        // OpAltTabConstructor opFourCon = new OpAltTabConstructor(OperatorNumber.FOUR);
       // OpAltTabConstructor opFiveCon = new OpAltTabConstructor(OperatorNumber.FIVE);
       // OpAltTabConstructor opSixCon = new OpAltTabConstructor(OperatorNumber.SIX);
        EffectsTabConstructor effTabCon = new EffectsTabConstructor();

        tabs.get(0).setContent(opOneCon.getLayout());
        tabs.get(1).setContent(opTwoCon.getLayout());
       // tabs.get(2).setContent(opThreeCon.getLayout());
       // tabs.get(3).setContent(opFourCon.getLayout());
      //  tabs.get(4).setContent(opFiveCon.getLayout());
      //  tabs.get(5).setContent(opSixCon.getLayout());

        tabs.get(8).setContent(effTabCon.getLayout());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
