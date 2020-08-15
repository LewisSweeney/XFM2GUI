package uk.ac.aber.lsweeney.controls;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class WaveControl extends ParameterControl {

    ComboBox<String> waveComboBox;
    ArrayList<Image> waveImageArrayList = new ArrayList<>();
    ImageView waveImageView;
    BorderPane borderPane;

    public WaveControl(String name, String paramID, int maxVal) {
        super(name, paramID, maxVal);
        waveComboBox = new ComboBox<>();
        waveImageView = new ImageView();
        constructLayout();
    }

    private void constructLayout() {

        for (int i = 0; i < 8; i++) {
            String url = "/images/waves/w" + i + ".png";
            waveImageArrayList.add(new Image(String.valueOf(this.getClass().getResource(url))));
            waveComboBox.getItems().add("Shape " + (i + 1));
        }

        waveComboBox.getSelectionModel().selectedItemProperty().addListener(
                (options, oldValue, newValue) -> onWaveSelected(waveComboBox.getSelectionModel().getSelectedIndex()));
        waveComboBox.getSelectionModel().selectFirst();


        waveImageView.setImage(waveImageArrayList.get(0));
        waveImageView.setFitHeight(80);
        waveImageView.setFitWidth(100);
        waveImageView.getStyleClass().add("wave-image");

        paramName.getStyleClass().add("param-label");

        VBox imageBox = new VBox(waveImageView);
        imageBox.getStyleClass().add("wave-image");

        borderPane = new BorderPane();
        borderPane.setTop(paramName);
        borderPane.setCenter(imageBox);
        borderPane.setBottom(waveComboBox);
        BorderPane.setAlignment(paramName, Pos.CENTER);
        BorderPane.setAlignment(waveComboBox, Pos.CENTER);
        borderPane.getStyleClass().add("control");
    }

    /**
     * Changes the image and parameter value for the relevant wave control
     *
     * @param index index of selected item in combobox
     */
    private void onWaveSelected(int index) {
        paramField.setValue(index);
        waveImageView.setImage(waveImageArrayList.get(index));
    }

    @Override
    public BorderPane getLayout() {

        return borderPane;
    }

    @Override
    public ComboBox<String> getWaves() {
        return waveComboBox;
    }

    @Override
    public void setWaveImage(int val) {
        waveImageView.setImage(waveImageArrayList.get(val));
    }

    @Override
    public ImageView getWaveImage() {
        return waveImageView;
    }
}
