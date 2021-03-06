package uk.ac.cam.groupseven.weatherapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

import java.awt.*;

/**
 * Google guice module.
 *
 * @see <a href="https://github.com/google/guice/wiki/GettingStarted">https://github.com/google/guice/wiki/GettingStarted</a>
 */
public class SettingsModule implements Module {
    @Override
    public void configure(Binder binder) {
        // Bind screen stuff like size and window titles
        binder.bind(Dimension.class).annotatedWith(Names.named("screenDimension"))
                .toInstance(new Dimension(700, 1132));
        binder.bind(String.class).annotatedWith(Names.named("windowTitle")).toInstance("Weather App");
        binder.bind(Integer.class).annotatedWith(Names.named("morningHour")).toInstance(8);
        binder.bind(Integer.class).annotatedWith(Names.named("afternoonHour")).toInstance(16);
    }
}
