package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.datasources.ViewModelSource;
import uk.ac.cam.groupseven.weatherapp.viewmodels.CrestViewModel;

import javax.swing.*;
import java.nio.file.Path;
import java.util.TreeMap;

public class CrestScreen implements Screen {

    @Inject
    ViewModelSource<CrestViewModel> crestSource;
    private JPanel panel;
    private JTable crestTable;
    private JButton returnHomeButton;

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public Disposable start() {
        return
                crestSource
                        .getViewModel(getRefreshObservable())
                        .subscribe(viewModel -> updateScreen(viewModel));


    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(returnHomeButton).map(x -> ScreenLayout.Direction.DOWN);
    }


    private void updateScreen(CrestViewModel viewModel) {
        TreeMap<String, Path> crests = viewModel.crests;
    }

    private Observable<Object> getRefreshObservable() {
        return Observable.just(new Object());
    }

}
