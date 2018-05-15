package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.CrestSource;

import javax.swing.*;
import java.nio.file.Path;

public class UserCrestViewModelSource implements ViewModelSource<ImageIcon> {
    @Inject
    CrestSource crestSource;

    @Inject
    @Named("crestDirectory")
    private Path imageDirectory;

    @Override
    public Observable<ImageIcon> getViewModel(Observable<Object> refresh) {

        return crestSource.getUserCrests().map(crest ->
                new ImageIcon(imageDirectory.resolve(crest.getCode() + ".gif").toAbsolutePath().toString()));
    }
}
