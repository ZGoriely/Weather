package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import hu.akarnokd.rxjava2.swing.SwingSchedulers;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.CrestSource;
import uk.ac.cam.groupseven.weatherapp.models.Crest;
import uk.ac.cam.groupseven.weatherapp.viewmodels.CrestViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;

import javax.swing.*;
import java.nio.file.Path;
import java.util.TreeMap;

public class CrestViewModelSource implements ViewModelSource<Loadable<CrestViewModel>> {
    @Inject
    CrestSource crestSource;

    @Inject
    @Named("crestDirectory")
    private Path imageDirectory;

    @Override
    public Observable<Loadable<CrestViewModel>> getViewModel(Observable<Object> refresh) {
        return refresh.flatMapSingle(r ->
                crestSource
                        .getAllCrests()
                        .toList()
                        .map(crests ->
                        {
                            TreeMap<String, ImageIcon> images = new TreeMap<>();
                            for (Crest crest : crests) {
                                images.put(crest.getCode(),
                                        new ImageIcon(imageDirectory.resolve(crest.getCode() + ".gif").toAbsolutePath().toString()));
                            }
                            return images;

                        })
                        .map(CrestViewModel::new)
                        .map(Loadable::new)
                        .onErrorReturn(Loadable::new)
                        .observeOn(SwingSchedulers.edt())

        );
    }
}
