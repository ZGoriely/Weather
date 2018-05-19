package uk.ac.cam.groupseven.weatherapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

import java.awt.*;
import java.util.Calendar;

public class SettingsModule implements Module {
    @Override
    public void configure(Binder binder) {
        // Bind screen stuff like size and window titles
        binder.bind(Dimension.class).annotatedWith(Names.named("screenDimension"))
                .toInstance(new Dimension(700, 1132));
        binder.bind(Calendar.class).toInstance(Calendar.getInstance());
        binder.bind(String.class).annotatedWith(Names.named("windowTitle")).toInstance("Weather App");
        binder.bind(Integer.class).annotatedWith(Names.named("morningHour")).toInstance(8);
        binder.bind(Integer.class).annotatedWith(Names.named("afternoonHour")).toInstance(16);
    }
}
