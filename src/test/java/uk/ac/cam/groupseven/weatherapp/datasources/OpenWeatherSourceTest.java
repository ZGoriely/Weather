package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Guice;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.ApplicationModule;

public class OpenWeatherSourceTest {
    @Test
    public void getWeatherNow() throws Exception {
        OpenWeatherSource openWeatherSource = Guice.createInjector(new ApplicationModule()).getInstance(OpenWeatherSource.class);
        System.out.println("Current -> "+openWeatherSource.getWeatherNow().blockingFirst());
    }

    @Test
    public void getWeatherInHours() throws Exception {
        int h = 6;
        OpenWeatherSource openWeatherSource = Guice.createInjector(new ApplicationModule()).getInstance(OpenWeatherSource.class);
        System.out.println("In "+h+" hours -> "+openWeatherSource.getWeatherInHours(6).blockingFirst());
    }

    @Test
    public void getWeatherInDays() throws Exception {
        int tm = 10;
        int days = 5;
        OpenWeatherSource openWeatherSource = Guice.createInjector(new ApplicationModule()).getInstance(OpenWeatherSource.class);
        System.out.println("In "+days+" day(s) at "+tm+":00 -> "+openWeatherSource.getWeatherInDays(days, tm).blockingFirst());
    }

}