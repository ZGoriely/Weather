package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.ImplementedBy;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.WaterLevel;

@ImplementedBy(WaterLevelSourceImpl.class)
public interface WaterLevelSource {
    Observable<WaterLevel> getWaterLevelNow();
}
