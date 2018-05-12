package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.datasources.ViewModelSource;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeWeather;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class HomeScreen implements Screen {
    @Inject
    ViewModelSource<HomeWeather> homeWeatherSource;
    private JPanel panel;
    private JTextPane weatherText;
    private JTextArea flagText;
    private JButton refreshButton;
    private JButton crestButton;
    private JButton leftButton;
    private JButton rightButton;
    private JButton additionalInformationButton;

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public Disposable start() {
        return
                homeWeatherSource
                        .getViewModel(getRefreshObservable())
                        .subscribe(viewModel -> updateScreen(viewModel));


    }

    private void updateScreen(HomeWeather viewModel) {
        if (viewModel.loading) {
            flagText.setText("Loading");
            weatherText.setText("");
        } else if (viewModel.error != null) {
            flagText.setText("An error occurred");
            weatherText.setText("");
        } else {
            flagText.setText(viewModel.flagText);
            weatherText.setText(viewModel.weatherText);
        }

    }

    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT)
                .mergeWith(SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT));

    }

    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .concatWith(SwingObservable.actions(refreshButton))//Refresh when button pressed
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }

}
