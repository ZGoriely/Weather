package uk.ac.cam.groupseven.weatherapp.mocking;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import uk.ac.cam.groupseven.weatherapp.datasources.CrestSource;
import uk.ac.cam.groupseven.weatherapp.models.Crest;

import java.util.prefs.BackingStoreException;

public class CrestSourceMock implements CrestSource {
    BehaviorSubject<Crest> subject = BehaviorSubject.createDefault(Crest.PELBY);

    @Override
    public void setNewCrest(Crest crest) throws BackingStoreException {
        subject.onNext(crest);
        System.out.println("Set crest event:" + crest.toString());
    }

    @Override
    public Observable<Crest> getAllCrests() {
        return Observable.fromArray(Crest.values());
    }

    @Override
    public Observable<Crest> getUserCrests() {
        return subject;
    }
}
