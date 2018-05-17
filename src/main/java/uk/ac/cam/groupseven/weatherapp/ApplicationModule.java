package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import uk.ac.cam.groupseven.weatherapp.viewmodels.*;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.*;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(Dimension.class).annotatedWith(Names.named("screenDimension"))
                .toInstance(new Dimension(700, 1132)); //Set screen size
        binder.bind(Path.class).annotatedWith(Names.named("crestDirectory"))
                .toInstance(Paths.get("./res/crests"));

        binder.bind(new TypeLiteral<ViewModelSource<Loadable<HomeViewModel>>>() {
        }).to(HomeViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<HourViewModel>>>() {
        }).to(HoursViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<CrestViewModel>>>() {
        }).to(CrestViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<Loadable<DaysViewModel>>>() {
        }).to(DaysViewModelSource.class);
        binder.bind(new TypeLiteral<ViewModelSource<ImageIcon>>() {
        }).to(UserCrestViewModelSource.class);

        // bind Urls
        try {
            binder.bind(URL.class).annotatedWith(Names.named("cucbcLightingUrl"))
                    .toInstance(new URL("http://www.cucbc.org/darkness.xml"));
            binder.bind(URL.class).annotatedWith(Names.named("cucbcFlagUrl"))
                    .toInstance(new URL("http://www.cucbc.org/flag.xml"));
            binder.bind(URL.class).annotatedWith(Names.named("openWeatherApiUrl"))
                    .toInstance(new URL("http://api.openweathermap.org/data/2.5/forecast?APPID=8b35f0643a9e43fac171d05738bd2b8d&id=2653941&units=metric&mode=xml"));
            binder.bind(URL.class).annotatedWith(Names.named("waterLevelSourceUrl"))
                    .toInstance(new URL("https://environment.data.gov.uk/flood-monitoring/id/measures/E60501-level-stage-i-15_min-mASD/readings?latest"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
