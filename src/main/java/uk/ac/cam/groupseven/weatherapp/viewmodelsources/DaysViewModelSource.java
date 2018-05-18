package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingSchedulers;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.OpenWeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.DaysViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class DaysViewModelSource implements ViewModelSource<Loadable<DaysViewModel>> {
    @Inject
    private OpenWeatherSource weatherApiSource;

    @Override
    public Observable<Loadable<DaysViewModel>> getViewModel(Observable<Object> refresh) {
        return Observable.range(0, 24)
                .flatMap(x -> weatherApiSource.getWeatherInDays(x, 0)) /* TODO SORT THIS OUT - HAVE ARBITRARILY USED 00:00 AS TIME OF DAY- NEED TO GET NATHAN TO MAKE IT DO MORNING AND EVENING TIMES */
                .toList()
                .map(this::buildModel)
                .toObservable()
                .observeOn(SwingSchedulers.edt());
    }

    private Loadable<DaysViewModel> buildModel(List<Weather> weatherList) {
        ArrayList<LocalDateTime> weatherTimes = new ArrayList<>();
        ArrayList<Float> temperatures = new ArrayList<>();
        ArrayList<Float> windSpeeds = new ArrayList<>();

        for (Weather weather : weatherList) {
            weatherTimes.add(weather.fromTime.plusHours(1)); /* Add 1 hour to get to approx middle of valid time range (actually 2/3) */
            temperatures.add(weather.temperature);
            windSpeeds.add(weather.wind.speedMPS);
        }
        return new Loadable<>(new DaysViewModel(weatherTimes, temperatures, windSpeeds));

    }
}
