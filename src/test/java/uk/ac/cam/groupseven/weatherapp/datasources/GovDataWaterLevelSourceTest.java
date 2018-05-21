package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Guice;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.modules.ApplicationModule;
import uk.ac.cam.groupseven.weatherapp.modules.IconsModule;
import uk.ac.cam.groupseven.weatherapp.modules.SettingsModule;
import uk.ac.cam.groupseven.weatherapp.modules.UrlsModule;

public class GovDataWaterLevelSourceTest {
    @Test
    public void getWaterLevelNow() {
        GovDataWaterLevelSource waterLevelSource = Guice.createInjector(new ApplicationModule(),
                new UrlsModule(),
                new IconsModule(),
                new SettingsModule()).getInstance(GovDataWaterLevelSource.class);

        System.out.println(waterLevelSource.getWaterLevelNow().blockingFirst().getLevel());
    }
}
