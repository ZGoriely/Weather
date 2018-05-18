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
                        .flatMap(x -> weatherApiSource.getWeatherInHours(x * 3))
                        .map(this::buildWeather)
                        .toList()
                        .map(this::buildModel)
                        .map(Loadable<HourViewModel>::new)
                        .onErrorReturn(Loadable::new)
        )
                .observeOn(SwingSchedulers.edt());
    }

    private HourViewModel buildModel(List<HourlyWeather> hourlyWeathers) {

        String timeText = new SimpleDateFormat("HH:mm").format(calendar.getTime());
        return new HourViewModel(timeText, hourlyWeathers);
    }

    private HourlyWeather buildWeather(Weather weather) {
        String timeText = weather.fromTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String temperatureText = String.format("%s°C", weather.temperature);
        String windText = String.format("%s m/s", weather.wind.speedMPS);
        return new HourlyWeather(timeText, temperatureText, windText);

    }
}
