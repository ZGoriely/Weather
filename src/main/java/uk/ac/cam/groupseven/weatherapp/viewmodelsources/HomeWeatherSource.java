package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.RowingInfoSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeWeather;

public class HomeWeatherSource implements ViewModelSource<HomeWeather> {
    @Inject
    private RowingInfoSource rowingInfoSource;
    @Inject
    private WeatherSource weatherSource;

    public Observable<HomeWeather> getViewModel(Observable<Object> refresh) {
        return refresh.flatMap(x ->
                Observable
                        .just(HomeWeather.Loading()) //Return loading followed by the actual data
                        .concatWith(
                                rowingInfoSource.getFlagStatus()//Get flag and observe result
                                        .flatMap(
                                                flagStatus ->
                                                        weatherSource.getWeatherNow()//Get weather and observe result
                                                                .map(weather -> buildModel(flagStatus, weather))
                                        )

                        )

        );
    }

    private HomeWeather buildModel(FlagStatus flagStatus, Weather weather) {
        String flagText = "The colour is " + flagStatus.getDisplayName();

        String weatherText = "Unknown weather";
        if (weather.precipitation != null) {
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
        }

        return new HomeWeather(flagText, weatherText);
    }


}
