package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import uk.ac.cam.groupseven.weatherapp.modules.ApplicationModule;
import uk.ac.cam.groupseven.weatherapp.modules.IconsModule;
import uk.ac.cam.groupseven.weatherapp.modules.SettingsModule;
import uk.ac.cam.groupseven.weatherapp.modules.UrlsModule;
import uk.ac.cam.groupseven.weatherapp.styles.StyleManager;

import javax.swing.*;


public class MainForm {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new ApplicationModule(),
                new IconsModule(),
                new SettingsModule(),
                new UrlsModule());

        JFrame frame = new JFrame(injector.getInstance(Key.get(String.class, Names.named("windowTitle"))));
        SlidingPanel panel = injector.getInstance(SlidingPanel.class);
        StyleManager.applyStyles(panel);
        panel.start();
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
