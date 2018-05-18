package uk.ac.cam.groupseven.weatherapp.mocking;

import com.google.inject.*;
import com.google.inject.name.Names;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.datasources.CrestSource;
import uk.ac.cam.groupseven.weatherapp.datasources.RowingInfoSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WaterLevelSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.*;
import uk.ac.cam.groupseven.weatherapp.viewmodels.*;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.CrestViewModelSource;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.UserCrestViewModelSource;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

public class MockedModule implements Module {
    public static Injector injector = Guice.createInjector(new MockedModule());

    @Override
    public void configure(Binder binder) {
        binder.bind(Dimension.class).annotatedWith(Names.named("screenDimension"))
                .toInstance(new Dimension(700, 1132)); //Set screen size

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

        binder.bind(CrestSource.class).to(CrestSourceMock.class);
        binder.bind(RowingInfoSource.class).to(RowingInfoSourceMock.class);
        binder.bind(WeatherSource.class).to(WeatherSourceMock.class);
        binder.bind(ScreenLayout.class).to(ScreenLayoutMock.class);
        binder.bind(Screen.class).to(ScreenMock.class);
        binder.bind(WaterLevelSource.class).to(WaterLevelSourceMock.class);

        binder.bind(Weather.class).toInstance(new Weather(Weather.Precipitation.NONE, 0, 0.0f, 0.0f, 0, new Wind(0.0f, ""), LocalDateTime.now(), LocalDateTime.now()));
        binder.bind(FlagStatus.class).toInstance(FlagStatus.GREEN);

        binder.bind(new TypeLiteral<Loadable<HomeViewModel>>() {
        }).toInstance(new Loadable<>(new HomeViewModel(FlagStatus.GREEN, 12.0f, 1.0f, "")));
      
        binder.bind(new TypeLiteral<Loadable<HourViewModel>>() {
        }).toInstance(new Loadable<>(new HourViewModel(
                "8:50",
                Arrays.asList(
                        new HourlyWeather("9:00", "10'C", "5m/s"),
                        new HourlyWeather("10:00", "13'C", "6m/s"),
                        new HourlyWeather("11:00", "16'C", "12m/s"),
                        new HourlyWeather("12:00", "17'C", "8m/s"),
                        new HourlyWeather("13:00", "18'C", "9m/s")
                ))));

        binder.bind(new TypeLiteral<Loadable<DaysViewModel>>(){}).toInstance(new Loadable<>(new DaysViewModel(

                Arrays.asList("1:00 - Sun", "2:00 - Sun", "3:00 - Sun", "4:00 - Sun", "5:00 - Sun"))));

        binder.bind(new TypeLiteral<Loadable<MoreViewModel>>() {
        }).toInstance(new Loadable<>(new MoreViewModel(
                new Weather(Weather.Precipitation.NONE, 0, 0.0f, 0.0f, 0, new Wind(0.0f, ""), LocalDateTime.now(), LocalDateTime.now()),
                new WaterLevel(0.95F),
                new LightingTimes("07:00", "21:00", "07:00", "21:00")
        )));


        binder.bind(new TypeLiteral<ViewModelSource<Loadable<HomeViewModel>>>() {
        }).to(new TypeLiteral<ViewModelSourceMock<Loadable<HomeViewModel>>>() {
        });
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<HourViewModel>>>() {
        }).to(new TypeLiteral<ViewModelSourceMock<Loadable<HourViewModel>>>() {
        });
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<CrestViewModel>>>() {
        }).to(CrestViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<DaysViewModel>>>() {
        }).to(new TypeLiteral<ViewModelSourceMock<Loadable<DaysViewModel>>>() {
        });
        binder.bind(new TypeLiteral<ViewModelSource<ImageIcon>>() {
        }).to(UserCrestViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<MoreViewModel>>>() {
        }).to(new TypeLiteral<ViewModelSourceMock<Loadable<MoreViewModel>>>() {
        });

    }
}
