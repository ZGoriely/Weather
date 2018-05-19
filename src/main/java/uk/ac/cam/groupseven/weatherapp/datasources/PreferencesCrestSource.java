package uk.ac.cam.groupseven.weatherapp.datasources;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import uk.ac.cam.groupseven.weatherapp.models.Crest;

import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

public class PreferencesCrestSource implements CrestSource {
    private static final String USER_CREST_PREF = "user_crest";
    private static final Preferences preferences = Preferences.userNodeForPackage(Crest.class);

    @Override
    public void setNewCrest(Crest crest) throws BackingStoreException {
        preferences.put(USER_CREST_PREF, crest.getCode());
        preferences.flush();
    }

    @Override
    public Observable<Crest> getAllCrests() {
        return Observable.fromArray(Crest.values())
                .subscribeOn(Schedulers.computation());
    }

    @Override
    public Observable<Crest> getUserCrests() {
        Observable<Crest> crestObservable = Observable.create(emitter ->
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
        return crestObservable.subscribeOn(Schedulers.io());
    }

}
