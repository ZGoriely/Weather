package uk.ac.cam.groupseven.weatherapp.datasources;

import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.models.Crest;

import java.util.List;

public class CrestSourceTest {
    @Test
    public void getUserCrests() throws Exception {
        CrestSource crestSource = new CrestSource();
        crestSource.setNewCrest(Crest.EMMANUEL);
        ConnectableObservable<Crest> observable = crestSource.getUserCrests().replay();
        Disposable disposable = observable.connect();
        crestSource.setNewCrest(Crest.QUEENS);
        List<Crest> crests = observable.take(2).toList().blockingGet();
        Assert.assertEquals(Crest.EMMANUEL, crests.get(0));
        Assert.assertEquals(Crest.QUEENS, crests.get(1));
        disposable.dispose();

    }

}