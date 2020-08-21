package uk.ac.aber.lsweeney;

import javafx.application.Application;

import static com.sun.javafx.application.LauncherImpl.launchApplication;

/**
 * Class used for bypassing JFX path issues
 * Simply runs the Main class' main method
 */
public class GUIStarter {
    // To run GUI
    public static void main(String[] args) {
       // System.setProperty("javafx.preloader", SplashScreen.class.getCanonicalName());
        Main.launch(Main.class, args);
    }

}
