package com.github.steadiestllama.xfm2gui.initializers;

/*

This file is part of XFM2GUI

Copyright 2020 Lewis Sweeney

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

import com.github.steadiestllama.xfm2gui.externalcode.IntField;
import javafx.scene.control.Tab;
import com.github.steadiestllama.xfm2gui.layouts.TabConstructor;
import com.github.steadiestllama.xfm2gui.enums.REQUIRED_TAB;

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
    ArrayList<String> egFilePaths = new ArrayList<>();
    ArrayList<String> effectsFilePaths = new ArrayList<>();
    ArrayList<String> modulationFilePaths = new ArrayList<>();

    public TabInit(){
        op1FilePaths.add("/data/parameters/operators/op1.txt");

        op2FilePaths.add("/data/parameters/operators/op2.txt");

        op3FilePaths.add("/data/parameters/operators/op3.txt");

        op4FilePaths.add("/data/parameters/operators/op4.txt");

        op5FilePaths.add("/data/parameters/operators/op5.txt");

        op6FilePaths.add("/data/parameters/operators/op6.txt");

        progFilePaths.add("/data/parameters/program/lfo.txt");
        progFilePaths.add("/data/parameters/program/other.txt");

        egFilePaths.add("/data/parameters/eg/pitcheg.txt");
        egFilePaths.add("/data/parameters/eg/amplitudeeg.txt");

        effectsFilePaths.add("/data/parameters/effects/am.txt");
        effectsFilePaths.add("/data/parameters/effects/bitcrusher.txt");
        effectsFilePaths.add("/data/parameters/effects/chorusflanger.txt");
        effectsFilePaths.add("/data/parameters/effects/decimator.txt");
        effectsFilePaths.add("/data/parameters/effects/delay.txt");
        effectsFilePaths.add("/data/parameters/effects/fxrouting.txt");
        effectsFilePaths.add("/data/parameters/effects/phaser.txt");
        effectsFilePaths.add("/data/parameters/effects/reverb.txt");
        effectsFilePaths.add("/data/parameters/effects/filter.txt");

        modulationFilePaths.add("/data/parameters/modulation/amplfo.txt");
        modulationFilePaths.add("/data/parameters/modulation/arpeggiator.txt");
        modulationFilePaths.add("/data/parameters/modulation/egbias.txt");
        modulationFilePaths.add("/data/parameters/modulation/perfcontrols.txt");
        modulationFilePaths.add("/data/parameters/modulation/pitch.txt");
        modulationFilePaths.add("/data/parameters/modulation/pitchlfo.txt");

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
        BufferedReader bReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/data/parameters/tablist.txt")));
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

        tabs.get(0).setContent(tabConstructor.getLayout(getOp1FilePaths(), getTabGroupValues(REQUIRED_TAB.op)));
        tabs.get(1).setContent(tabConstructor.getLayout(getOp2FilePaths(), getTabGroupValues(REQUIRED_TAB.op)));
        tabs.get(2).setContent(tabConstructor.getLayout(getOp3FilePaths(), getTabGroupValues(REQUIRED_TAB.op)));
        tabs.get(3).setContent(tabConstructor.getLayout(getOp4FilePaths(), getTabGroupValues(REQUIRED_TAB.op)));
        tabs.get(4).setContent(tabConstructor.getLayout(getOp5FilePaths(), getTabGroupValues(REQUIRED_TAB.op)));
        tabs.get(5).setContent(tabConstructor.getLayout(getOp6FilePaths(), getTabGroupValues(REQUIRED_TAB.op)));
        tabs.get(6).setContent(tabConstructor.getLayout(getProgFilePaths(), getTabGroupValues(REQUIRED_TAB.prog)));
        tabs.get(7).setContent(tabConstructor.getLayout(egFilePaths,getTabGroupValues(REQUIRED_TAB.eg)));
        tabs.get(8).setContent(tabConstructor.getLayout(getModulationFilePaths(), getTabGroupValues(REQUIRED_TAB.mod)));
        tabs.get(9).setContent(tabConstructor.getLayout(getEffectsFilePaths(), getTabGroupValues(REQUIRED_TAB.fx)));

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
            case op1 -> "/data/parameters/groupValues/operator1.txt";
            case op -> "/data/parameters/groupValues/operator.txt";
            case fx -> "/data/parameters/groupValues/effects.txt";
            case mod -> "/data/parameters/groupValues/modulation.txt";
            case prog -> "/data/parameters/groupValues/program.txt";
            case eg -> "/data/parameters/groupValues/eg.txt";
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
