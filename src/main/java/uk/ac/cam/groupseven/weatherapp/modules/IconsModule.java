package uk.ac.cam.groupseven.weatherapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Google guice module.
 *
 * @see <a href="https://github.com/google/guice/wiki/GettingStarted">https://github.com/google/guice/wiki/GettingStarted</a>
 */
public class IconsModule implements Module {
    @Override
    public void configure(Binder binder) {
        try {
            configureIcons(binder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configureIcons(Binder binder) throws IOException {
        // Bind directories
        binder.bind(Path.class).annotatedWith(Names.named("crestDirectory"))
                .toInstance(Paths.get("./res/crests"));
        binder.bind(Path.class).annotatedWith(Names.named("flagsDirectory"))
                .toInstance(Paths.get("./res/flag"));
        binder.bind(Path.class).annotatedWith(Names.named("refreshIcon"))
                .toInstance(Paths.get("./res/icons/refresh_white_18dp.png"));

        // Bind miscellaneous stuff
        int iconSize = 100;
        binder.bind(Integer.class).annotatedWith(Names.named("moreScreenIconSize"))
                .toInstance(iconSize);
        binder.bind(Dimension.class).annotatedWith(Names.named("refreshIconDimensions")).toInstance(
                new Dimension(60, 60));

        // Bind images
        ImageIcon waterIcon = new ImageIcon(ImageIO.read(new File("res/icons/waterLevel.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon precipitationIcon = new ImageIcon(ImageIO.read(new File("res/icons/precipitation.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon cloudsIcon = new ImageIcon(ImageIO.read(new File("res/icons/clouds.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon pressureIcon = new ImageIcon(ImageIO.read(new File("res/icons/pressure.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon humidityIcon = new ImageIcon(ImageIO.read(new File("res/icons/humidity.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon windIcon = new ImageIcon(ImageIO.read(new File("res/icons/windDirection.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon sunriseIcon = new ImageIcon(ImageIO.read(new File("res/icons/sunrise.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon sunsetIcon = new ImageIcon(ImageIO.read(new File("res/icons/sunset.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon thermometer = new ImageIcon(ImageIO.read(new File("res/icons/thermometer.png")).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        ImageIcon thermometerSmall = new ImageIcon(ImageIO.read(new File("res/icons/thermometer.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        ImageIcon wind = new ImageIcon(ImageIO.read(new File("res/icons/wind.png")).getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        ImageIcon windSmall = new ImageIcon(ImageIO.read(new File("res/icons/wind.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));

        binder.bind(ImageIcon.class).annotatedWith(Names.named("waterIcon")).toInstance(waterIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("precipitationIcon")).toInstance(precipitationIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("cloudsIcon")).toInstance(cloudsIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("pressureIcon")).toInstance(pressureIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("humidityIcon")).toInstance(humidityIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("windIcon")).toInstance(windIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("sunriseIcon")).toInstance(sunriseIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("sunsetIcon")).toInstance(sunsetIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("windBigIcon")).toInstance(thermometer);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("tempBigIcon")).toInstance(wind);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("windSmallIcon")).toInstance(windSmall);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("tempSmallIcon")).toInstance(thermometerSmall);
    }
}
