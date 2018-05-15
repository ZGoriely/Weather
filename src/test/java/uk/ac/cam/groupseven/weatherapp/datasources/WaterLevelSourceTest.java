package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Guice;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.ApplicationModule;

public class WaterLevelSourceTest {
    @Test
    public void getWaterLevelNow() {
        WaterLevelSourceImpl waterLevelSourceImpl = Guice.createInjector(new ApplicationModule()).getInstance(WaterLevelSourceImpl.class);
        System.out.println(waterLevelSourceImpl.getWaterLevelNow().blockingFirst().level);
    }
}
