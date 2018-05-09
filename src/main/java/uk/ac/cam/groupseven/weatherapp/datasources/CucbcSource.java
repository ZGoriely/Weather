package uk.ac.cam.groupseven.weatherapp.datasources;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;

public class CucbcSource {
    public Observable<FlagStatus> getFlagStatus() {
        return Observable.fromCallable(() -> {
            Thread.sleep(1000); //Pretend to do some work;
            return new FlagStatus(FlagStatus.Status.GREEN);
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }
}
