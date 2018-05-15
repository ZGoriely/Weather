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
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class MockedModule implements Module {
    public static Injector injector = Guice.createInjector(new MockedModule());

    @Override
    public void configure(Binder binder) {
        binder.bind(Dimension.class).annotatedWith(Names.named("screenDimension"))
                .toInstance(new Dimension(700, 1132)); //Set screen size

        binder.bind(Path.class).annotatedWith(Names.named("crestDirectory"))
                .toInstance(Paths.get("./res/crests"));

        binder.bind(CrestSource.class).to(CrestSourceMock.class);
        binder.bind(RowingInfoSource.class).to(RowingInfoSourceMock.class);
        binder.bind(WeatherSource.class).to(WeatherSourceMock.class);
        binder.bind(ScreenLayout.class).to(ScreenLayoutMock.class);
        binder.bind(Screen.class).to(ScreenMock.class);
        binder.bind(WaterLevelSource.class).to(WaterLevelSourceMock.class);

        binder.bind(Weather.class).toInstance(new Weather(Weather.Precipitation.NONE, 0, 0.0f, new Wind(0.0f, "")));
        binder.bind(FlagStatus.class).toInstance(FlagStatus.NONOPERATIONAL);
        binder.bind(HomeWeather.class).toInstance(new HomeWeather("The colour is Green", "Sunny skies"));
        binder.bind(HourWeather.class).toInstance(new HourWeather(
                Arrays.asList("1:00 - Sun", "2:00 - Sun", "3:00 - Sun", "4:00 - Sun", "5:00 - Sun")));
        binder.bind(MoreWeather.class).toInstance(new MoreWeather(
                new Weather(Weather.Precipitation.NONE, 0, 0.0f, new Wind(0.0f, "North")),
                new WaterLevel(0.5F),
                new LightingTimes("07:00", "21:00", "07:00", "21:00")
        ));

        binder.bind(new TypeLiteral<ViewModelSource<MoreWeather>>() {
        }).annotatedWith(Names.named("moreWeatherSource"))
                .to(new TypeLiteral<ViewModelSourceMock<MoreWeather>>() {
                });


        binder.bind(new TypeLiteral<ViewModelSource<HomeWeather>>() {
        }).to(new TypeLiteral<ViewModelSourceMock<HomeWeather>>() {
        });
        binder.bind(new TypeLiteral<ViewModelSource<HourWeather>>() {
        }).to(new TypeLiteral<ViewModelSourceMock<HourWeather>>() {
        });
        binder.bind(new TypeLiteral<ViewModelSource<CrestViewModel>>() {
        }).to(CrestViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<DaysWeather>>() {
        }).to(new TypeLiteral<ViewModelSourceMock<DaysWeather>>() {
        });
    }
}
