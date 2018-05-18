package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.styles.ApplyStyle;
import uk.ac.cam.groupseven.weatherapp.styles.BackgroundStyle;
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
    private JTextPane flagText;
    private JButton refreshButton;
    private JButton crestButton;
    private JButton leftButton;
    private JButton rightButton;
    private JButton additionalInformationButton;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel topPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel midPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel bottomPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JLabel tempIcon;
    private JTextPane tempText;
    private JTextPane windText;
    @ApplyStyle(BackgroundStyle.class)
    private JLabel windIcon;
    private JPanel weatherPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JLabel flagIcon;

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public Disposable start() {
        crestImageSource.getViewModel((getRefreshObservable())).subscribe(this::updateCrest);
        return
                homeWeatherSource
                        .getViewModel(getRefreshObservable())
                        .subscribe(this::updateScreen);


    }

    private void updateScreen(Loadable<HomeViewModel> viewModelLoadable) {
        HomeViewModel viewModel = viewModelLoadable.getViewModel();
        // Set refresh icon
        try {
            BufferedImage refreshImage = ImageIO.read(new File("res/icons/refresh.png"));
            ImageIcon refresh = new ImageIcon(refreshImage.getScaledInstance(60, 60, Image.SCALE_FAST));
            refreshButton.setIcon(refresh);
            refreshButton.setText("");
        } catch (IOException e) {
            refreshButton.setText("Refresh");
            refreshButton.setIcon(null);
        }

        // Set weather information
        if (viewModelLoadable.getLoading()) {
            flagText.setText("loading");
            tempText.setText("Temperature: ...");
            windText.setText("Wind Speed: ...");
        } else if (viewModelLoadable.getError() != null) {
            flagText.setText("Error");
            windText.setText("Error");
            tempText.setText("Error");
        } else {
            try {
                // Set temp icon
                BufferedImage thermometerImage = ImageIO.read(new File("res/icons/thermometer.png"));
                ImageIcon thermometer = new ImageIcon(thermometerImage.getScaledInstance(200, 200, Image.SCALE_FAST));
                tempIcon.setIcon(thermometer);

                //Set wind icon
                BufferedImage windImage = ImageIO.read(new File("res/icons/wind.png"));
                ImageIcon wind = new ImageIcon(windImage.getScaledInstance(150, 150, Image.SCALE_FAST));
                windIcon.setIcon(wind);

            } catch (IOException e) {
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

}
