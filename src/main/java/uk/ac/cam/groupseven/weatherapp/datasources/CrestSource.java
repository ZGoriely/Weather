package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.ImplementedBy;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.Crest;

import java.util.prefs.BackingStoreException;

@ImplementedBy(PreferencesCrestSource.class)
public interface CrestSource {
    void setNewCrest(Crest crest) throws BackingStoreException;

    Observable<Crest> getAllCrests();

    Observable<Crest> getUserCrests();
}
