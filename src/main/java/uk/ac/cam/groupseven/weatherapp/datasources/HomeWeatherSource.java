package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Inject;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeWeather;

public class HomeWeatherSource extends ViewModelSource<HomeWeather> {
    @Inject
    CucbcSource cucbcSource;
    @Inject
    WeatherApiSource weatherApiSource;

    public Observable<HomeWeather> getViewModel(Observable<Object> refresh) {
        return refresh.flatMap(x ->
                Observable
                        .just(HomeWeather.Loading()) //Return loading followed by the actual data
                        .concatWith(
                                cucbcSource.getFlagStatus()//Get flag and observe result
                                        .flatMap(
                                                flagStatus ->
                                                        weatherApiSource.getWeatherNow()//Get weather and observe result
                                                                .map(weather -> buildModel(flagStatus, weather))
                                        )

                        )

        );
    }

    private HomeWeather buildModel(FlagStatus flagStatus, Weather weather) {
        String flagText = "The colour is " + flagStatus.getDisplayName();

        String weatherText = "";
        switch (weather.precipitation) {
            case NONE:
                weatherText = "Sunny skies";
                break;
            case RAIN:
                weatherText = "Rainy skies";
                break;
            case SNOW:
                weatherText = "Its snowing";
                break;
        }

        return new HomeWeather(flagText, weatherText);
    }


}
