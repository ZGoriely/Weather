package uk.ac.cam.groupseven.weatherapp.mocking;

import com.google.inject.*;
import com.google.inject.name.Names;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.datasources.CucbcSource;
import uk.ac.cam.groupseven.weatherapp.datasources.ViewModelSource;
import uk.ac.cam.groupseven.weatherapp.datasources.OpenWeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.models.Wind;
import uk.ac.cam.groupseven.weatherapp.screens.Screen;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourWeather;

import java.awt.*;
import java.util.Arrays;

public class MockedModule implements Module {
    public static Injector injector = Guice.createInjector(new MockedModule());

    @Override
    public void configure(Binder binder) {
        binder.bind(Dimension.class).annotatedWith(Names.named("screenDimension"))
                .toInstance(new Dimension(500, 500)); //Set screen size

        binder.bind(CucbcSource.class).to(CucbcSourceMock.class);
        binder.bind(OpenWeatherSource.class).to(WeatherApiSourceMock.class);
        binder.bind(ScreenLayout.class).to(ScreenLayoutMock.class);
        binder.bind(Screen.class).to(ScreenMock.class);

        binder.bind(Weather.class).toInstance(new Weather(Weather.Precipitation.NONE, 0, 0.0f, new Wind(0.0f, "")));
        binder.bind(FlagStatus.class).toInstance(FlagStatus.GREEN);
        binder.bind(HomeWeather.class).toInstance(new HomeWeather("The flag is GREEN", "Sunny skies"));
        binder.bind(HourWeather.class).toInstance(new HourWeather(
                Arrays.asList("1:00 - Sun", "2:00 - Sun", "3:00 - Sun", "4:00 - Sun", "5:00 - Sun")));

        binder.bind(new TypeLiteral<ViewModelSource<HomeWeather>>() {
        }).to(new TypeLiteral<ViewModelSourceMock<HomeWeather>>() {
        });
        binder.bind(new TypeLiteral<ViewModelSource<HourWeather>>() {
        }).to(new TypeLiteral<ViewModelSourceMock<HourWeather>>() {
        });

    }
}
