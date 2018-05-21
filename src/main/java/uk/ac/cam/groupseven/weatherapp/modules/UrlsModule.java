package uk.ac.cam.groupseven.weatherapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Google guice module.
 *
 * @see <a href="https://github.com/google/guice/wiki/GettingStarted">https://github.com/google/guice/wiki/GettingStarted</a>
 */
public class UrlsModule implements Module {
    @Override
    public void configure(Binder binder) {
        try {
            configureUrls(binder);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void configureUrls(Binder binder) throws MalformedURLException {
        // Bind urls
        binder.bind(URL.class).annotatedWith(Names.named("cucbcLightingUrl"))
                .toInstance(new URL("http://www.cucbc.org/darkness.xml"));
        binder.bind(URL.class).annotatedWith(Names.named("cucbcFlagUrl"))
                .toInstance(new URL("http://www.cucbc.org/flag.xml"));
        binder.bind(URL.class).annotatedWith(Names.named("openWeatherApiUrl"))
                .toInstance(new URL("http://api.openweathermap.org/data/2.5/forecast?APPID=8b35f0643a9e43fac171d05738bd2b8d&id=2653941&units=metric&mode=xml"));
        binder.bind(URL.class).annotatedWith(Names.named("waterLevelSourceUrl"))
                .toInstance(new URL("https://environment.data.gov.uk/flood-monitoring/id/measures/E60501-level-stage-i-15_min-mASD/readings?latest"));

    }

}
