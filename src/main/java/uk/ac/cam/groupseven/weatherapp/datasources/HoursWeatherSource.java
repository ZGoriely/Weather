package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Inject;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourWeather;

import java.util.ArrayList;
import java.util.List;


public class HoursWeatherSource extends ViewModelSource<HourWeather> {
    @Inject
    WeatherApiSource weatherApiSource;

    @Override
    public Observable<HourWeather> getViewModel(Observable<Object> refresh) {
        return Observable.range(0, 7)
                .flatMap(x -> weatherApiSource.getWeatherInHours(x))
                .toList()
                .map(x -> buildModel(x))
                .toObservable();

    }

    private HourWeather buildModel(List<Weather> weatherList) {
        ArrayList<String> weatherTexts = new ArrayList<>();
        for (int i = 0; i < weatherList.size(); i++) {
            switch (weatherList.get(i).precipitation) {
                case RAIN:
                    weatherTexts.add(String.format("Day %s: Rain", i));
                    break;
                case SNOW:
                    weatherTexts.add(String.format("Day %s: Snow", i));
                    break;
                case NONE:
                    weatherTexts.add(String.format("Day %s: Sun", i));
                    break;
            }
        }
        return new HourWeather(weatherTexts);

    }
}
