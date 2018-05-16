package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.ImplementedBy;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.Weather;

@ImplementedBy(OpenWeatherSource.class)
public interface WeatherSource {
    Observable<Weather> getWeatherNow();

    //TODO: Implement methods for getting future weather
    Observable<Weather> getWeatherInHours(int hours);

    //TODO: Implement methods for getting future weather
    Observable<Weather> getWeatherInDays(int days, int timeInHours);
}
