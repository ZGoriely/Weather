package uk.ac.cam.groupseven.weatherapp.datasources;

import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.WaterLevel;

public interface WaterLevelSource {
    public Observable<WaterLevel> getWaterLevelNow();
}
