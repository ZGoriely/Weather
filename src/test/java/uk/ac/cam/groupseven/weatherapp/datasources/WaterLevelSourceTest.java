package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Guice;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.ApplicationModule;

public class WaterLevelSourceTest {
    @Test
    public void getWaterLevelNow() {
        WaterLevelSource waterLevelSource = Guice.createInjector(new ApplicationModule()).getInstance(WaterLevelSource.class);
        System.out.println(waterLevelSource.getWaterLevelNow().blockingFirst().level);
    }
}
