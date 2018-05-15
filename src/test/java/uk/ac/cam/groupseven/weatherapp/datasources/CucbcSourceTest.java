package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Guice;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.ApplicationModule;

public class CucbcSourceTest {
    @Test
    public void getLightingStatus() {
        CucbcSource cucbcSource = Guice.createInjector(new ApplicationModule()).getInstance(CucbcSource.class);
        System.out.println(cucbcSource.getLightingStatus().blockingFirst().todayDownTime);
    }

    @Test
    public void getFlagStatus() {
        CucbcSource cucbcSource = Guice.createInjector(new ApplicationModule()).getInstance(CucbcSource.class);
        System.out.println(cucbcSource.getFlagStatus().blockingFirst().getDisplayName());
    }
}