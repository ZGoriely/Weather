package uk.ac.cam.groupseven.weatherapp.mocking;

import com.google.inject.*;
import com.google.inject.name.Names;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.datasources.CrestSource;
import uk.ac.cam.groupseven.weatherapp.datasources.RowingInfoSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.models.Wind;
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

        binder.bind(Integer.class).annotatedWith(Names.named("morningHour")).toInstance(8);
        binder.bind(Integer.class).annotatedWith(Names.named("afternoonHour")).toInstance(16);

        binder.bind(CrestSource.class).to(CrestSourceMock.class);
        binder.bind(RowingInfoSource.class).to(RowingInfoSourceMock.class);
        binder.bind(WeatherSource.class).to(WeatherSourceMock.class);
        binder.bind(ScreenLayout.class).to(ScreenLayoutMock.class);
        binder.bind(Screen.class).to(ScreenMock.class);

        binder.bind(Weather.class).toInstance(new Weather(Weather.Precipitation.NONE, 0, 0.0f, 0.0f, 0, new Wind(0.0f, ""), LocalDateTime.now(), LocalDateTime.now()));
        binder.bind(FlagStatus.class).toInstance(FlagStatus.GREEN);

        binder.bind(new TypeLiteral<Loadable<HomeViewModel>>() {
        }).toInstance(new Loadable<>(new HomeViewModel(FlagStatus.GREEN, 12.0f, 1.0f, "")));
        binder.bind(new TypeLiteral<Loadable<HourViewModel>>() {
        }).toInstance(new Loadable<>(new HourViewModel(
                Arrays.asList("1:00 - Sun", "2:00 - Sun", "3:00 - Sun", "4:00 - Sun", "5:00 - Sun"))));
        binder.bind(new TypeLiteral<Loadable<DaysViewModel>>() {
        }).toInstance(new Loadable<>(new DaysViewModel(
                Arrays.asList(
                        new DayWeather("24/05", "5'C", "6m/s", "20'C", "7m/s"),
                        new DayWeather("25/05", "4'C", "9m/s", "17'C", "2m/s"),
                        new DayWeather("26/05", "2'C", "3m/s", "25'C", "2m/s"),
                        new DayWeather("27/05", "10'C", "4m/s", "18'C", "9m/s"),
                        new DayWeather("28/05", "7'C", "8m/s", "21'C", "2m/s"),
                        new DayWeather("29/05", "4'C", "6m/s", "15'C", "5m/s")

                ))));

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

    }
}
