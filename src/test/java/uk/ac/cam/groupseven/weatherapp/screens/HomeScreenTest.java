package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Guice;
import com.google.inject.Injector;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.modules.MockingModule;
import uk.ac.cam.groupseven.weatherapp.styles.StyleManager;

import javax.swing.*;

public class HomeScreenTest {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MockingModule());
        JFrame frame = new JFrame("uk.ac.cam.groupseven.weatherapp.HomeScreenTest");
        Screen screen = injector.getInstance(HomeScreen.class);
        StyleManager.applyStyles(screen);
        screen.start();
        frame.setContentPane(screen.getPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
