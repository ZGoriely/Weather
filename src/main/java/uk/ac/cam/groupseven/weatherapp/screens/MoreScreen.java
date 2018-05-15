package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.viewmodels.MoreWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.concurrent.TimeUnit;

public class MoreScreen implements Screen {
    @Inject
    @Named("moreWeatherSource")
    private ViewModelSource<MoreWeather> moreWeatherSource;


    private JButton upButton;
    private JPanel panel;
    private JTable infoTable;

    @Override
    public Disposable start() {
        return moreWeatherSource.getViewModel(getRefreshObservable()).subscribe(this::updateTable);
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(upButton).map(x -> ScreenLayout.Direction.UP);
    }

    @Override
    public JPanel getPanel() { return panel; }

    private void updateTable(MoreWeather weather) {
        // Rows: precipitation, cloud cover, pressure, humidity, wind direction, other
        if (weather.error != null || weather.loading) {
            return;
        }

        TableModel model = infoTable.getModel();

        model.setValueAt("Water Level", 0, 0);
        model.setValueAt(weather.waterLevel.level, 0, 1);
        model.setValueAt("Precipitation", 1, 0);
        model.setValueAt(weather.weather.precipitation.toString(), 1, 1);
        model.setValueAt("Cloud Cover", 2, 0);
        model.setValueAt(weather.weather.cloudCover, 2, 1);
        model.setValueAt("Temperature", 3, 0);
        model.setValueAt(weather.weather.temperature, 3, 1);
        model.setValueAt("Wind Direction", 4, 0);
        model.setValueAt(weather.weather.wind.direction, 4, 1);

    }

    private void createUIComponents() {
        infoTable = new JTable(5, 2);
        infoTable.getModel().setValueAt("circle", 0, 0);
        infoTable.getModel().setValueAt("square", 1, 0);
        infoTable.getModel().setValueAt("triangle", 2, 0);
        infoTable.getModel().setValueAt("red", 0, 1);
        infoTable.getModel().setValueAt("green", 1, 1);
        infoTable.getModel().setValueAt("blue", 2, 1);
    }

    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }
}