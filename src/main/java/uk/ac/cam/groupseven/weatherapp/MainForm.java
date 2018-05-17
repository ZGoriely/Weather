package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import uk.ac.cam.groupseven.weatherapp.styles.StyleManager;

import javax.swing.*;


public class MainForm {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ApplicationModule());
        JFrame frame = new JFrame("uk.ac.cam.groupseven.weatherapp.MainForm");
        SlidingPanel panel = injector.getInstance(SlidingPanel.class);
        StyleManager.applyStyles(panel);
        panel.start();
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
