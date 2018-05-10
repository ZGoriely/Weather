package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.datasources.ViewModelSource;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourWeather;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class HoursScreen implements Screen {
    @Inject
    ViewModelSource<HourWeather> viewModelSource;
    private JPanel panel;
    private JList list;
    private JButton leftButton;
    private JButton rightButton;
    private JTextPane dateText;

    @Override
    public Disposable start() {
        return viewModelSource.getViewModel(getRefreshObservable()).subscribe(x -> updateScreen(x));
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT)
                .mergeWith(SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT));
    }

    private void updateScreen(HourWeather viewModel) {
        list.setListData(new Object[0]);
        if (viewModel.loading) {
            //TODO
        } else if (viewModel.error != null) {
            //TODO
        } else {
            list.setListData(viewModel.precipitationTexts.toArray());

        }

    }

    public JPanel getPanel() {
        return panel;
    }


    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }
}
