package uk.ac.aber.lsweeney.initializers;

import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uk.ac.aber.lsweeney.externalcode.IntField;
import uk.ac.aber.lsweeney.sceneconstructors.TabConstructor;
import uk.ac.aber.lsweeney.enums.OPERATOR_NUM;
import uk.ac.aber.lsweeney.enums.REQUIRED_TAB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Initializes and prepares all tabs ready for use, including creating the parameter control layout for each tab
 */
public class TabInit {

    ArrayList<String> op1FilePaths = new ArrayList<>();
    ArrayList<String> op2FilePaths = new ArrayList<>();
    ArrayList<String> op3FilePaths = new ArrayList<>();
    ArrayList<String> op4FilePaths = new ArrayList<>();
    ArrayList<String> op5FilePaths = new ArrayList<>();
    ArrayList<String> op6FilePaths = new ArrayList<>();
    ArrayList<String> progFilePaths = new ArrayList<>();
    ArrayList<String> effectsFilePaths = new ArrayList<>();
    ArrayList<String> modulationFilePaths = new ArrayList<>();

    public TabInit(){
        op1FilePaths.add("/xfm2/parameters/operators/op1.txt");

        op2FilePaths.add("/xfm2/parameters/operators/op2.txt");

        op3FilePaths.add("/xfm2/parameters/operators/op3.txt");

        op4FilePaths.add("/xfm2/parameters/operators/op4.txt");

        op5FilePaths.add("/xfm2/parameters/operators/op5.txt");

        op6FilePaths.add("/xfm2/parameters/operators/op6.txt");

        progFilePaths.add("/xfm2/parameters/program/lfo.txt");
        progFilePaths.add("/xfm2/parameters/program/pitcheg.txt");
        progFilePaths.add("/xfm2/parameters/program/other.txt");
        progFilePaths.add("/xfm2/parameters/program/amplitudeeg.txt");

        effectsFilePaths.add("/xfm2/parameters/effects/am.txt");
        effectsFilePaths.add("/xfm2/parameters/effects/bitcrusher.txt");
        effectsFilePaths.add("/xfm2/parameters/effects/chorusflanger.txt");
        effectsFilePaths.add("/xfm2/parameters/effects/decimator.txt");
        effectsFilePaths.add("/xfm2/parameters/effects/delay.txt");
        effectsFilePaths.add("/xfm2/parameters/effects/fxrouting.txt");
        effectsFilePaths.add("/xfm2/parameters/effects/phaser.txt");
        effectsFilePaths.add("/xfm2/parameters/effects/reverb.txt");
        effectsFilePaths.add("/xfm2/parameters/effects/filter.txt");

        modulationFilePaths.add("/xfm2/parameters/modulation/amplfo.txt");
        modulationFilePaths.add("/xfm2/parameters/modulation/arpeggiator.txt");
        modulationFilePaths.add("/xfm2/parameters/modulation/egbias.txt");
        modulationFilePaths.add("/xfm2/parameters/modulation/perfcontrols.txt");
        modulationFilePaths.add("/xfm2/parameters/modulation/pitch.txt");
        modulationFilePaths.add("/xfm2/parameters/modulation/pitchlfo.txt");

    }

    /**
     * Gets filepaths for operator 1
     * @return ArrayList containing filepaths
     */
    public ArrayList<String> getOp1FilePaths() {
        return op1FilePaths;
    }

    /**
     * Gets filepaths for operator 2
     * @return ArrayList containing filepaths
     */
    public ArrayList<String> getOp2FilePaths() {
        return op2FilePaths;
    }

    /**
     * Gets filepaths for operator 3
     * @return ArrayList containing filepaths
     */
    public ArrayList<String> getOp3FilePaths() {
        return op3FilePaths;
    }

    /**
     * Gets filepaths for operator 4
     * @return ArrayList containing filepaths
     */
    public ArrayList<String> getOp4FilePaths() {
        return op4FilePaths;
    }

    /**
     * Gets filepaths for operator 5
     * @return ArrayList containing filepaths
     */
    public ArrayList<String> getOp5FilePaths() {
        return op5FilePaths;
    }

    /**
     * Gets filepaths for operator 6
     * @return ArrayList containing filepaths
     */
    public ArrayList<String> getOp6FilePaths() {
        return op6FilePaths;
    }

    /**
     * Gets filepaths for program tab
     * @return ArrayList containing filepaths
     */
    public ArrayList<String> getProgFilePaths() {
        return progFilePaths;
    }

    /**
     * Gets filepaths for effects tab
     * @return ArrayList containing filepaths
     */
    public ArrayList<String> getEffectsFilePaths() {
        return effectsFilePaths;
    }

    /**
     * Gets filepaths for modulation tab
     * @return ArrayList containing filepaths
     */
    public ArrayList<String> getModulationFilePaths() {
        return modulationFilePaths;
    }

    /**
     * Constructs the tabs, taking parameter IntFields to be assigned IDs
     * @param paramFields IntField arraylist
     * @return ArrayList of tabs ready for use
     */
    public ArrayList<Tab> getTabs(ArrayList<IntField> paramFields){


        ArrayList<Tab> tabs = new ArrayList<>();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/xfm2/parameters/tablist.txt")));
        try {
            String line = bReader.readLine();
            while (line != null) {
                Tab t = new Tab(line);
                t.setClosable(false);
                tabs.add(t);
                line = bReader.readLine();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        ImageView cog = new ImageView(new Image (this.getClass().getResourceAsStream("/images/cogwheel.png")));
        cog.setFitHeight(20);
        cog.setFitWidth(20);

        TabConstructor tabConstructor = new TabConstructor();

        tabs.get(0).setContent(tabConstructor.getLayout(getOp1FilePaths(), getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op1));
        tabs.get(1).setContent(tabConstructor.getLayout(getOp2FilePaths(), getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op2));
        tabs.get(2).setContent(tabConstructor.getLayout(getOp3FilePaths(), getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op3));
        tabs.get(3).setContent(tabConstructor.getLayout(getOp4FilePaths(), getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op4));
        tabs.get(4).setContent(tabConstructor.getLayout(getOp5FilePaths(), getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op5));
        tabs.get(5).setContent(tabConstructor.getLayout(getOp6FilePaths(), getTabGroupValues(REQUIRED_TAB.op), OPERATOR_NUM.op6));
        tabs.get(6).setContent(tabConstructor.getLayout(getProgFilePaths(), getTabGroupValues(REQUIRED_TAB.prog), OPERATOR_NUM.no));
        tabs.get(7).setContent(tabConstructor.getLayout(getModulationFilePaths(), getTabGroupValues(REQUIRED_TAB.mod), OPERATOR_NUM.no));
        tabs.get(8).setContent(tabConstructor.getLayout(getEffectsFilePaths(), getTabGroupValues(REQUIRED_TAB.fx), OPERATOR_NUM.no));
        tabs.get(9).setContent(null);

        paramFields.addAll(tabConstructor.getIntFields());

        return tabs;
    }

    /**
     * Gets the required group values, which are how the various components of each tab are grouped.
     *
     * @param r Required tab type, Enum
     * @return Returns the values of the required groups as an arraylist of strings that contain groupings.
     */
    public ArrayList<String> getTabGroupValues(REQUIRED_TAB r) {

        String filepath = switch (r) {
            case op1 -> "/xfm2/parameters/groupValues/operator1.txt";
            case op -> "/xfm2/parameters/groupValues/operator.txt";
            case fx -> "/xfm2/parameters/groupValues/effects.txt";
            case mod -> "/xfm2/parameters/groupValues/modulation.txt";
            case prog -> "/xfm2/parameters/groupValues/program.txt";
        };

        BufferedReader bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filepath)));
        ArrayList<String> programGroupValues = new ArrayList<>();
        try {
            String line = bReader.readLine();
            while (line != null) {
                programGroupValues.add(line);
                line = bReader.readLine();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return programGroupValues;
    }
}
