package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Guice;
import org.junit.Before;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.modules.ApplicationModule;
import uk.ac.cam.groupseven.weatherapp.modules.IconsModule;
import uk.ac.cam.groupseven.weatherapp.modules.SettingsModule;
import uk.ac.cam.groupseven.weatherapp.modules.UrlsModule;

public class OpenWeatherSourceTest {
    OpenWeatherSource openWeatherSource;

    @Before
    public void setUp() throws Exception {
        openWeatherSource = Guice.createInjector(new ApplicationModule(),
                new IconsModule(),
                new UrlsModule(),
                new SettingsModule())
                .getInstance(OpenWeatherSource.class);
    }

    @Test
    public void getWeatherNow() throws Exception {
        System.out.println("Current -> "+openWeatherSource.getWeatherNow().blockingFirst());
    }

    @Test
    public void getWeatherInHours() throws Exception {
        int h = 6;
        System.out.println("In "+h+" hours -> "+openWeatherSource.getWeatherInHours(6).blockingFirst());
    }

    @Test
    public void getWeatherInDays() throws Exception {
        int tm = 10;
        int days = 4;

        System.out.println("In "+days+" day(s) at "+tm+":00 -> "+openWeatherSource.getWeatherInDays(days, tm).blockingFirst());
    }

}