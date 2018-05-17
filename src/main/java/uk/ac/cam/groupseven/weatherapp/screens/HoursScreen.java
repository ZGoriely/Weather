package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.styles.ApplyStyle;
import uk.ac.cam.groupseven.weatherapp.styles.BackgroundStyle;
import uk.ac.cam.groupseven.weatherapp.styles.ButtonStyle;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class HoursScreen implements Screen {
    @Inject
    ViewModelSource<HourWeather> viewModelSource;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;
    @ApplyStyle(BackgroundStyle.class)
    private JList<Object> list;
    @ApplyStyle(ButtonStyle.class)
    private JButton leftButton;
    @ApplyStyle(ButtonStyle.class)
    private JButton rightButton;
    @ApplyStyle(BackgroundStyle.class)
    private JTextPane dateText;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel midPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel topPanel;

    @Override
    public Disposable start() {
        return EmptyDisposable.INSTANCE;
        //return viewModelSource.getViewModel(getRefreshObservable()).subscribe(x -> updateScreen(x));
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
        } else if (viewModel.precipitationTexts != null) {
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
