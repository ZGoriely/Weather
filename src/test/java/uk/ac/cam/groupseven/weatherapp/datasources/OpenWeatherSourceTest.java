package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Guice;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.ApplicationModule;

public class OpenWeatherSourceTest {
    @Test
    public void getWeatherNow() throws Exception {
        OpenWeatherSource openWeatherSource = Guice.createInjector(new ApplicationModule()).getInstance(OpenWeatherSource.class);
        System.out.println(openWeatherSource.getWeatherNow().blockingFirst());
    }

    @Test
    public void getWeatherInHours() throws Exception {
        OpenWeatherSource openWeatherSource = Guice.createInjector(new ApplicationModule()).getInstance(OpenWeatherSource.class);
        System.out.println(openWeatherSource.getWeatherInHours(6).blockingFirst());
    }

}