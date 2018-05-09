package uk.ac.cam.groupseven.weatherapp.mocking;

import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.CucbcSource;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;

public class CucbcSourceMock extends CucbcSource {

    @Override
    public Observable<FlagStatus> getFlagStatus() {
        return Observable.just(new FlagStatus(FlagStatus.Status.GREEN));
    }
}
