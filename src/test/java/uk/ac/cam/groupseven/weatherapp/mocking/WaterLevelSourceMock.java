package uk.ac.cam.groupseven.weatherapp.mocking;

import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.WaterLevelSource;
import uk.ac.cam.groupseven.weatherapp.models.WaterLevel;

public class WaterLevelSourceMock implements WaterLevelSource {
    @Override
    public Observable<WaterLevel> getWaterLevelNow() {
        return Observable.just(new WaterLevel(0.9F));
    }
}
