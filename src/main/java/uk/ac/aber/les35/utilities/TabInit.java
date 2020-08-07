package uk.ac.aber.les35.utilities;

import javafx.scene.control.Tab;
import uk.ac.aber.les35.externalcode.IntField;
import uk.ac.aber.les35.tabconstructors.TabConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
        op1FilePaths.add("/parameters/operators/op1.txt");

        op2FilePaths.add("/parameters/operators/op2.txt");

        op3FilePaths.add("/parameters/operators/op3.txt");

        op4FilePaths.add("/parameters/operators/op4.txt");

        op5FilePaths.add("/parameters/operators/op5.txt");

        op6FilePaths.add("/parameters/operators/op6.txt");

        progFilePaths.add("/parameters/program/lfo.txt");
        progFilePaths.add("/parameters/program/pitcheg.txt");
        progFilePaths.add("/parameters/program/other.txt");
        progFilePaths.add("/parameters/program/amplitudeeg.txt");

        effectsFilePaths.add("/parameters/effects/am.txt");
        effectsFilePaths.add("/parameters/effects/bitcrusher.txt");
        effectsFilePaths.add("/parameters/effects/chorusflanger.txt");
        effectsFilePaths.add("/parameters/effects/decimator.txt");
        effectsFilePaths.add("/parameters/effects/delay.txt");
        effectsFilePaths.add("/parameters/effects/fxrouting.txt");
        effectsFilePaths.add("/parameters/effects/phaser.txt");
        effectsFilePaths.add("/parameters/effects/reverb.txt");
        effectsFilePaths.add("/parameters/effects/filter.txt");

        modulationFilePaths.add("/parameters/modulation/amplfo.txt");
        modulationFilePaths.add("/parameters/modulation/arpeggiator.txt");
        modulationFilePaths.add("/parameters/modulation/egbias.txt");
        modulationFilePaths.add("/parameters/modulation/perfcontrols.txt");
        modulationFilePaths.add("/parameters/modulation/pitch.txt");
        modulationFilePaths.add("/parameters/modulation/pitchlfo.txt");

    }
    public ArrayList<String> getOp1FilePaths() {
        return op1FilePaths;
    }

    public ArrayList<String> getOp2FilePaths() {
        return op2FilePaths;
    }

    public ArrayList<String> getOp3FilePaths() {
        return op3FilePaths;
    }

    public ArrayList<String> getOp4FilePaths() {
        return op4FilePaths;
    }

    public ArrayList<String> getOp5FilePaths() {
        return op5FilePaths;
    }

    public ArrayList<String> getOp6FilePaths() {
        return op6FilePaths;
    }

    public ArrayList<String> getProgFilePaths() {
        return progFilePaths;
    }

    public ArrayList<String> getEffectsFilePaths() {
        return effectsFilePaths;
    }

    public ArrayList<String> getModulationFilePaths() {
        return modulationFilePaths;
    }

    public ArrayList<Tab> getTabs(ArrayList<IntField> paramFields){


        ArrayList<Tab> tabs = new ArrayList<>();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/parameters/tablist.txt")));
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
            case op1 -> "/parameters/groupValues/operator1.txt";
            case op -> "/parameters/groupValues/operator.txt";
            case fx -> "/parameters/groupValues/effects.txt";
            case mod -> "/parameters/groupValues/modulation.txt";
            case prog -> "/parameters/groupValues/program.txt";
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
