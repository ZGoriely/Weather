package uk.ac.cam.groupseven.weatherapp.mocking;

import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.CucbcSource;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.models.LightingTimes;

public class CucbcSourceMock extends CucbcSource {
    @Override
    public Observable<LightingTimes> getLightingStatus() {
        return Observable.just(new LightingTimes("07:00", "20:00", "07:00", "20:00"));
    }

    @Override
    public Observable<FlagStatus> getFlagStatus() {
        return Observable.just(FlagStatus.GREEN);
    }
}
