package uk.ac.cam.groupseven.weatherapp.datasources;

import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.Crest;

import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

public class CrestSource {
    private static final String USER_CREST_PREF = "user_crest";
    private static final Preferences preferences = Preferences.userNodeForPackage(Crest.class);

    public static void setNewCrest(Crest crest) throws BackingStoreException {
        preferences.put(USER_CREST_PREF, crest.getCode());
        preferences.flush();
    }

    public Observable<Crest> getUserCrests() {
        return Observable.create(emitter ->
                {
                    Crest current = Crest.getCrestFromCode(preferences.get(USER_CREST_PREF, ""));
                    emitter.onNext(current);
                    PreferenceChangeListener listener = evt -> {
                        if (evt.getKey().equals(USER_CREST_PREF)) {
                            emitter.onNext(Crest.getCrestFromCode(evt.getNewValue()));
                        }
                    };
                    preferences.addPreferenceChangeListener(listener);
                    emitter.setCancellable(() -> preferences.removePreferenceChangeListener(listener));
                }

        );
    }

}
