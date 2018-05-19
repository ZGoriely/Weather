package uk.ac.cam.groupseven.weatherapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        binder.bind(Path.class).annotatedWith(Names.named("crestDirectory"))
                .toInstance(Paths.get("./res/crests"));
        binder.bind(Path.class).annotatedWith(Names.named("flagsDirectory"))
                .toInstance(Paths.get("./res/flag"));

        binder.bind(ImageIcon.class).annotatedWith(Names.named("windSmallIcon")).toInstance(
                new ImageIcon(new ImageIcon("./res/icons/wind.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT))
        );
        binder.bind(ImageIcon.class).annotatedWith(Names.named("tempSmallIcon")).toInstance(
                new ImageIcon(new ImageIcon("./res/icons/thermometer.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT))
        );

        BufferedImage thermometerImage = ImageIO.read(new File("res/icons/thermometer.png"));
        ImageIcon thermometer = new ImageIcon(thermometerImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH));

        BufferedImage windImage = ImageIO.read(new File("res/icons/wind.png"));
        ImageIcon wind = new ImageIcon(windImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH));

        binder.bind(ImageIcon.class).annotatedWith(Names.named("windBigIcon")).toInstance(thermometer);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("tempBigIcon")).toInstance(wind);


        binder.bind(Dimension.class).annotatedWith(Names.named("refreshIconDimensions")).toInstance(
                new Dimension(60, 60));

        binder.bind(Path.class).annotatedWith(Names.named("refreshIcon"))
                .toInstance(Paths.get("./res/icons/refresh_white_18dp.png"));

        int iconSize = 100;
        binder.bind(Integer.class).annotatedWith(Names.named("moreScreenIconSize"))
                .toInstance(iconSize);

        ImageIcon waterIcon = new ImageIcon(ImageIO.read(new File("res/icons/waterLevel.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon precipitationIcon = new ImageIcon(ImageIO.read(new File("res/icons/precipitation.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon cloudsIcon = new ImageIcon(ImageIO.read(new File("res/icons/clouds.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon pressureIcon = new ImageIcon(ImageIO.read(new File("res/icons/pressure.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon humidityIcon = new ImageIcon(ImageIO.read(new File("res/icons/humidity.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon windIcon = new ImageIcon(ImageIO.read(new File("res/icons/windDirection.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon sunriseIcon = new ImageIcon(ImageIO.read(new File("res/icons/sunrise.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        ImageIcon sunsetIcon = new ImageIcon(ImageIO.read(new File("res/icons/sunset.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));

        binder.bind(ImageIcon.class).annotatedWith(Names.named("waterIcon")).toInstance(waterIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("precipitationIcon")).toInstance(precipitationIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("cloudsIcon")).toInstance(cloudsIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("pressureIcon")).toInstance(pressureIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("humidityIcon")).toInstance(humidityIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("windIcon")).toInstance(windIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("sunriseIcon")).toInstance(sunriseIcon);
        binder.bind(ImageIcon.class).annotatedWith(Names.named("sunsetIcon")).toInstance(sunsetIcon);

    }

}
