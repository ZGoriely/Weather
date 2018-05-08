package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import uk.ac.cam.groupseven.weatherapp.datasources.HomeWeatherSource;
import uk.ac.cam.groupseven.weatherapp.datasources.HoursWeatherSource;
import uk.ac.cam.groupseven.weatherapp.datasources.ViewModelSource;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourWeather;

import java.awt.*;

public class ApplicationModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(Dimension.class).annotatedWith(Names.named("screenDimension"))
                .toInstance(new Dimension(500, 500)); //Set screen size

        binder.bind(new TypeLiteral<ViewModelSource<HomeWeather>>() {
        }).to(HomeWeatherSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<HourWeather>>() {
        }).to(HoursWeatherSource.class);

    }
}
