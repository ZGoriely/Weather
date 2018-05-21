package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.ImplementedBy;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.WaterLevel;

@ImplementedBy(GovDataWaterLevelSource.class)
public interface WaterLevelSource {
    Observable<WaterLevel> getWaterLevelNow();
}
