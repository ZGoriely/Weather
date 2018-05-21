package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.ImplementedBy;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.Weather;

@ImplementedBy(OpenWeatherSource.class)
public interface WeatherSource { // Defines an interface for a class that gets weather from an API
    Observable<Weather> getWeatherNow(); // Get the current weather (as an observable of the weather class)

    Observable<Weather> getWeatherInHours(int hours); // Get the weather in a given number of hours

    Observable<Weather> getWeatherInDays(int days, int timeInHours); /* Get the weather the given number of days in the future,
                                                                        at the given number of hours past midnight on that day */
}
