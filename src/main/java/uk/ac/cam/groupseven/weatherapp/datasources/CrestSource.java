package uk.ac.cam.groupseven.weatherapp.datasources;

import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.Crest;

import java.util.prefs.BackingStoreException;

public interface CrestSource {
    void setNewCrest(Crest crest) throws BackingStoreException;

    Observable<Crest> getUserCrests();
}
