package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import hu.akarnokd.rxjava2.swing.SwingSchedulers;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;
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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private Disposable loadingObservable = EmptyDisposable.INSTANCE;

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public Disposable start() {
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
            try {
                // Set temp icon
                BufferedImage thermometerImage = ImageIO.read(new File("res/icons/thermometer.png"));
                ImageIcon thermometer = new ImageIcon(thermometerImage.getScaledInstance(150, 150, Image.SCALE_FAST));
                tempIcon.setIcon(thermometer);

                //Set wind icon
                BufferedImage windImage = ImageIO.read(new File("res/icons/wind.png"));
                ImageIcon wind = new ImageIcon(windImage.getScaledInstance(150,150, Image.SCALE_FAST));
                windIcon.setIcon(wind);

                // Set flag icon
                BufferedImage flagImage = ImageIO.read(new File("res/flag/" + flagStatus.getCode() + ".png"));
                ImageIcon flag = new ImageIcon(flagImage.getScaledInstance(250, 250, Image.SCALE_FAST));
                flagIcon.setIcon(flag);
            }
            catch (IOException e) {
                System.out.println("Image not found");
                e.printStackTrace();
            }
        }

    }

    private void updateCrest(ImageIcon viewModel) {
        crestButton.setIcon(viewModel);
    }

    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT)
                .mergeWith(SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT))
                .mergeWith(SwingObservable.actions(crestButton).map(x -> ScreenLayout.Direction.UP));

    }

    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .concatWith(SwingObservable.actions(refreshButton))//Refresh when button pressed
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }

    private void setLoading(boolean loading) {
        loadingObservable.dispose();
        while (loadingIcon.getRotation() > 360) {
            loadingIcon.setRotation(loadingIcon.getRotation() - 360);
        }
        if (loading) {
            loadingObservable = Observable
                    .intervalRange(loadingIcon.getRotation() / 10, (loadingIcon.getRotation() + 360), 0, 10, TimeUnit.MILLISECONDS)
                    .repeat()
                    .subscribeOn(SwingSchedulers.edt())
                    .subscribe(x -> {
                        loadingIcon.setRotation(Math.toIntExact(x) * 10);
                        refreshButton.repaint();
                    });
        } else {
            loadingObservable = Observable
                    .intervalRange(loadingIcon.getRotation() / 10, 360 / 10, 0, 10, TimeUnit.MILLISECONDS)
                    .subscribeOn(SwingSchedulers.edt())
                    .subscribe(x -> {
                        loadingIcon.setRotation(Math.toIntExact(x) * 10);
                        refreshButton.repaint();
                    });
        }
    }

}
