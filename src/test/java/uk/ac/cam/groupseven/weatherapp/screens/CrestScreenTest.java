package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Guice;
import com.google.inject.Injector;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.mocking.MockedModule;
import uk.ac.cam.groupseven.weatherapp.styles.StyleManager;

import javax.swing.*;

public class CrestScreenTest {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MockedModule());
        JFrame frame = new JFrame("uk.ac.cam.groupseven.weatherapp.CrestScreenTest");
        Screen screen = injector.getInstance(CrestScreen.class);
        StyleManager.applyStyles(screen);
        screen.start();
        screen.getScreenChanges().subscribe(x -> {
        });
        frame.setContentPane(screen.getPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
