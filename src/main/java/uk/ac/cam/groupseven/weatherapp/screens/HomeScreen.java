package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
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
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
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
            ImageIcon refresh = new ImageIcon(refreshImage.getScaledInstance(60,60, Image.SCALE_FAST));
            refreshButton.setIcon(refresh);
            refreshButton.setText("");
        }
        catch (IOException e) {
            refreshButton.setText("Refresh");
            refreshButton.setIcon(null);
        }

        // Set weather information
        if (viewModelLoadable.getLoading()) {
            flagText.setText("loading");
            tempText.setText("Temperature: ...");
            windText.setText("Wind Speed: ...");
        }

        else if (viewModelLoadable.getError() != null) {
            flagText.setText("Error");
            windText.setText("Error");
            tempText.setText("Error");
        }

        else {
            // Get weather info and set text
            FlagStatus flagStatus = viewModel.getFlag();
            flagText.setText("Flag: " + flagStatus.getDisplayName());
            tempText.setText("Temperature: "+ Float.toString(viewModel.getTemperature()));
            windText.setText("Wind Speed: " + Float.toString(viewModel.getWindSpeed()));
            try {
                // Set temp icon
                BufferedImage thermometerImage = ImageIO.read(new File("res/icons/thermometer.png"));
                ImageIcon thermometer = new ImageIcon(thermometerImage.getScaledInstance(150,150, Image.SCALE_FAST));
                tempIcon.setIcon(thermometer);

                //Set wind icon
                BufferedImage windImage = ImageIO.read(new File("res/icons/wind.png"));
                ImageIcon wind = new ImageIcon(windImage.getScaledInstance(150,150, Image.SCALE_FAST));
                windIcon.setIcon(wind);

                // Set flag icon
                BufferedImage flagImage = ImageIO.read(new File("res/flag/"+flagStatus.getCode()+".png"));
                ImageIcon flag = new ImageIcon(flagImage.getScaledInstance(250,250, Image.SCALE_FAST));
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

}
