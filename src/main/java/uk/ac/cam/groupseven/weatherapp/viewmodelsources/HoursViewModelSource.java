package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingSchedulers;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.OpenWeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourlyWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;


public class HoursViewModelSource implements ViewModelSource<Loadable<HourViewModel>> {
    @Inject
    private OpenWeatherSource weatherApiSource;
    @Inject
    private Calendar calendar;

    @Override
    public Observable<Loadable<HourViewModel>> getViewModel(Observable<Object> refresh) {
        return refresh.flatMapSingle(o ->
                Observable.range(0, 6)
                        .concatMap(x -> weatherApiSource.getWeatherInHours(x * 3)) // Get the weather
                        .map(this::buildWeather)
                        .toList()
                        .map(this::buildModel)
                        .map(Loadable<HourViewModel>::new)
                        .onErrorReturn(Loadable::new)
        ).observeOn(SwingSchedulers.edt());
    }

    private HourViewModel buildModel(List<HourlyWeather> hourlyWeathers) {
        // Set up the time text and call the constructor that takes a list
        String timeText = new SimpleDateFormat("HH:mm").format(calendar.getTime());
        LocalDateTime timeNow = LocalDateTime.now();
        return new HourViewModel(timeText, hourlyWeathers);
    }

    private HourlyWeather buildWeather(Weather weather) {
        // Set up text and call the constructor that takes only the texts
        String timeText = weather.getFromTime().plusHours(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm"));
        String temperatureText = String.format("%.1f°C", weather.getTemperature());
        String windText = String.format("%.1f m/s", weather.getWind().getSpeedMPS());
        return new HourlyWeather(timeText, temperatureText, windText);
    }
}