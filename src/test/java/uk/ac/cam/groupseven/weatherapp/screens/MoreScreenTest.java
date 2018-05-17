package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Guice;
import com.google.inject.Injector;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.mocking.MockedModule;
import uk.ac.cam.groupseven.weatherapp.styles.StyleManager;

import javax.swing.*;

public class MoreScreenTest {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MockedModule());
        JFrame frame = new JFrame("uk.ac.cam.groupseven.weatherapp.MoreScreenTest");
        Screen screen = injector.getInstance(MoreScreen.class);
        StyleManager.applyStyles(screen);
        screen.start();
        frame.setContentPane(screen.getPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
