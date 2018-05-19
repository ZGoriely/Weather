package uk.ac.cam.groupseven.weatherapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IconsModule implements Module {

    @Override
    public void configure(Binder binder) {

        binder.bind(Path.class).annotatedWith(Names.named("crestDirectory"))
                .toInstance(Paths.get("./res/crests"));

        binder.bind(ImageIcon.class).annotatedWith(Names.named("windSmallIcon")).toInstance(
                new ImageIcon(new ImageIcon("./res/icons/wind.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT))
        );
        binder.bind(ImageIcon.class).annotatedWith(Names.named("tempSmallIcon")).toInstance(
                new ImageIcon(new ImageIcon("./res/icons/thermometer.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT))
        );


        binder.bind(Path.class).annotatedWith(Names.named("refreshIcon"))
                .toInstance(Paths.get("./res/icons/refresh_white_18dp.png"));
    }
}
