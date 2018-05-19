package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Guice;
import com.google.inject.Injector;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.modules.MockingModule;
import uk.ac.cam.groupseven.weatherapp.styles.StyleManager;

import javax.swing.*;

public class DaysScreenTest {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MockingModule());
        JFrame frame = new JFrame("uk.ac.cam.groupseven.weatherapp.DaysScreenTest");
        Screen screen = injector.getInstance(DaysScreen.class);
        StyleManager.applyStyles(screen);
        screen.start();
        frame.setContentPane(screen.getPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
