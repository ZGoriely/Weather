package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.Crest;
import uk.ac.cam.groupseven.weatherapp.viewmodels.CrestViewModel;

import javax.swing.*;
import java.nio.file.Path;
import java.util.TreeMap;

public class CrestViewModelSource implements ViewModelSource<CrestViewModel> {
    @Inject
    CrestSource crestSource;

    @Inject
    @Named("crestDirectory")
    private Path imageDirectory;

    @Override
    public Observable<CrestViewModel> getViewModel(Observable<Object> refresh) {
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
                        .onErrorReturn(CrestViewModel::new)

        );
    }
}
