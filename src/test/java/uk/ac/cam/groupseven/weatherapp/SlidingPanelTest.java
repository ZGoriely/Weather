package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import uk.ac.cam.groupseven.weatherapp.modules.MockingModule;

import javax.swing.*;

public class SlidingPanelTest {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MockingModule());
        JFrame frame = new JFrame("uk.ac.cam.groupseven.weatherapp.SlidingPanelTest");
        SlidingPanel panel = injector.getInstance(SlidingPanel.class);
        panel.start();
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
