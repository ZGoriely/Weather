package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.schedulers.Schedulers;
import uk.ac.cam.groupseven.weatherapp.LoadingIcon;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.styles.ApplyStyle;
import uk.ac.cam.groupseven.weatherapp.styles.BackgroundStyle;
import uk.ac.cam.groupseven.weatherapp.styles.ButtonStyle;
import uk.ac.cam.groupseven.weatherapp.styles.CenterTextStyle;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class HomeScreen implements Screen {
    @Inject
    ViewModelSource<Loadable<HomeViewModel>> homeWeatherSource;
    @Inject
    ViewModelSource<ImageIcon> crestImageSource;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;

    @ApplyStyle(CenterTextStyle.class)
    private JTextPane flagText;
    @ApplyStyle(ButtonStyle.class)
    private JButton refreshButton;
    @ApplyStyle(ButtonStyle.class)
    private JButton crestButton;
    @ApplyStyle(ButtonStyle.class)
    private JButton leftButton;
    @ApplyStyle(ButtonStyle.class)
    private JButton rightButton;
    @ApplyStyle(ButtonStyle.class)

    private JButton additionalInformationButton;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel topPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel midPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel bottomPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JLabel tempIcon;

    @ApplyStyle(CenterTextStyle.class)
    private JTextPane tempText;
    @ApplyStyle(CenterTextStyle.class)
    private JTextPane windText;
    @ApplyStyle(BackgroundStyle.class)
    private JLabel windIcon;
    @ApplyStyle(ButtonStyle.class)
    private JPanel weatherPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JLabel flagIcon;
    @Inject
    private LoadingIcon loadingIcon;
    private boolean loadingAnimating = false;
    private Disposable loadingDisposable = EmptyDisposable.INSTANCE;
    @Inject
    @Named("tempBigIcon")
    private ImageIcon thermometer;
    @Inject
    @Named("windBigIcon")
    private ImageIcon wind;

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public Disposable start() {
        // Set temp icon
        tempIcon.setIcon(thermometer);
        //Set wind icon
        windIcon.setIcon(wind);
        refreshButton.setIcon(loadingIcon);
        setLoading(true);
        crestImageSource.getViewModel((getRefreshObservable())).subscribe(this::updateCrest);
        return
                homeWeatherSource
                        .getViewModel(getRefreshObservable())
                        .subscribe(this::updateScreen);


    }

    private void updateScreen(Loadable<HomeViewModel> viewModelLoadable) {
        HomeViewModel viewModel = viewModelLoadable.getViewModel();

        if (viewModelLoadable.getLoading()) {
            flagText.setText("loading");
            tempText.setText("Temperature: ...");
            windText.setText("Wind Speed: ...");
            setLoading(true);

        } else if (viewModelLoadable.getError() != null) {
            setLoading(false);
            flagText.setText("Error");
            windText.setText("Error");
            tempText.setText("Error");
        } else {
            setLoading(false);
            // Get weather info and set text
            FlagStatus flagStatus = viewModel.getFlag();
            flagText.setText("Flag: " + flagStatus.getDisplayName());
            tempText.setText("Temperature: " + Float.toString(viewModel.getTemperature()));
            windText.setText("Wind Speed: " + Float.toString(viewModel.getWindSpeed()));
            flagIcon.setIcon(viewModel.getFlagImage());

        }

    }

    private void updateCrest(ImageIcon viewModel) {
        crestButton.setIcon(viewModel);
    }

    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT)
                .mergeWith(SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT))
                .mergeWith(SwingObservable.actions(crestButton).map(x -> ScreenLayout.Direction.UP))
                .mergeWith(SwingObservable.actions(additionalInformationButton).map(x -> ScreenLayout.Direction.DOWN));

    }

    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .concatWith(SwingObservable.actions(refreshButton))//Refresh when button pressed
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }

    private void setLoading(boolean loading) {
        loadingDisposable.dispose();
        int startRotation = loadingIcon.getRotation();
        Disposable loadingObservable;
        if (loading) {
            loadingAnimating = true;
            loadingObservable = Observable
                    .intervalRange(0, 360, 0, 10, TimeUnit.MILLISECONDS)
                    .repeat().subscribeOn(Schedulers.computation())
                    .subscribe(x -> loadingIcon.setRotation(startRotation + Math.toIntExact(x) * 10),
                            Throwable::printStackTrace,
                            () -> loadingAnimating = false);


        } else {
            loadingAnimating = true;
            loadingObservable = Observable
                    .intervalRange(startRotation / 10, (360 - startRotation) / 10, 0, 10, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .subscribe(x -> loadingIcon.setRotation(Math.toIntExact(x) * 10),
                            Throwable::printStackTrace,
                            () -> loadingAnimating = false);
        }

        Disposable repaintDisposable = Observable
                .interval(0, 10, TimeUnit.MILLISECONDS)
                .takeWhile(x -> loadingAnimating)
                .observeOn(Schedulers.computation())
                .subscribe(x -> refreshButton.repaint());

        loadingDisposable = new CompositeDisposable(loadingObservable, repaintDisposable);
    }

}
