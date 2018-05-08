package uk.ac.cam.groupseven.weatherapp.datasources;


import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import uk.ac.cam.groupseven.weatherapp.models.Weather;

public class WeatherApiSource {
    public Observable<Weather> getWeatherNow() {
        return Observable.fromCallable(() -> {
            //TODO: Replace with API call
            Thread.sleep(1000); //Pretend to do some work;
            return new Weather(Weather.Precipitation.NONE);
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }

    public Observable<Weather> getWeatherInHours(int hours) {
        return Observable.fromCallable(() -> {
            //TODO: Replace with API call
            Thread.sleep(1000); //Pretend to do some work;
            return new Weather(Weather.Precipitation.values()[hours % 3]); //Fast and accurate weather prediction
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }

}
