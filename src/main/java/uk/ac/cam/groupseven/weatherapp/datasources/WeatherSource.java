package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.ImplementedBy;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.Weather;

@ImplementedBy(OpenWeatherSource.class)
public interface WeatherSource {
    Observable<Weather> getWeatherNow();

    Observable<Weather> getWeatherInHours(int hours);

    Observable<Weather> getWeatherInDays(int days, int timeInHours);
}
