package uk.ac.cam.groupseven.weatherapp.datasources;

import io.reactivex.observers.TestObserver;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.models.Crest;

public class CrestSourceTest {
    @Test
    public void getUserCrests() throws Exception {
        CrestSource crestSource = new PreferencesCrestSource();
        crestSource.setNewCrest(Crest.EMMANUEL);
        TestObserver<Crest> test = crestSource.getUserCrests().test();
        test.awaitCount(1);
        crestSource.setNewCrest(Crest.QUEENS);
        test.assertValueAt(0, Crest.EMMANUEL);
        test.assertValueAt(1, Crest.QUEENS);
    }

}