package uk.ac.cam.groupseven.weatherapp.modules;

import com.google.inject.*;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.datasources.CrestSource;
import uk.ac.cam.groupseven.weatherapp.datasources.RowingInfoSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WaterLevelSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WeatherSource;
import uk.ac.cam.groupseven.weatherapp.mocking.*;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.models.Wind;
import uk.ac.cam.groupseven.weatherapp.viewmodels.*;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.CrestViewModelSource;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.UserCrestViewModelSource;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Arrays;

public class MockingModule implements Module {
    public static Injector injector = Guice.createInjector(new MockingModule());

    @Override
    public void configure(Binder binder) {
        new IconsModule().configure(binder);
        new SettingsModule().configure(binder);
        binder.bind(CrestSource.class).to(CrestSourceMock.class);
        binder.bind(RowingInfoSource.class).to(RowingInfoSourceMock.class);
        binder.bind(WeatherSource.class).to(WeatherSourceMock.class);
        binder.bind(ScreenLayout.class).to(ScreenLayoutMock.class);
        binder.bind(Screen.class).to(ScreenMock.class);
        binder.bind(WaterLevelSource.class).to(WaterLevelSourceMock.class);

        binder.bind(Weather.class).toInstance(new Weather(Weather.Precipitation.NONE, 0, 12.0f, 0.0f, 0, new Wind(1.0f, ""), LocalDateTime.now(), LocalDateTime.now()));
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

        binder.bind(new TypeLiteral<Loadable<MoreViewModel>>() {
        }).toInstance(new Loadable<>(new MoreViewModel(
                "Waterlevel:\n 10m",
                "Precipitation:\n5%",
                "Cloud Cover:\n21%",
                "Pressure:\n1027hPa",
                "Humidity:\n6%",
                "Wind Direction:\nN",
                "Sunrise:\n 7:00",
                "Sunset:\n 18:00"
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
