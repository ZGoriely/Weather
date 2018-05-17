package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingSchedulers;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.RowingInfoSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.models.Wind;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;

import java.util.concurrent.TimeUnit;

public class HomeViewModelSource implements ViewModelSource<Loadable<HomeViewModel>> {
    @Inject
    private RowingInfoSource rowingInfoSource;
    @Inject
    private WeatherSource weatherSource;

    public Observable<Loadable<HomeViewModel>> getViewModel(Observable<Object> refresh) {
        return refresh
                //.throttleFirst(6, TimeUnit.SECONDS)
                .flatMap(x ->
                Observable
                        .just(new Loadable<HomeViewModel>()) //Return loading followed by the actual data
                        .concatWith(
                                rowingInfoSource.getFlagStatus()//Get flag and observe result
                                        .flatMap(
                                                flagStatus ->
                                                        weatherSource.getWeatherNow()//Get weather and observe result
                                                                .map(weather -> buildModel(flagStatus, weather))
                                        )

                        )

        )
                .observeOn(SwingSchedulers.edt());
    }

    private Loadable<HomeViewModel> buildModel(FlagStatus flagStatus, Weather weather) {
        float temperature = 0.0f;
        float windSpeed = 0.0f;
        String windDir = "None";


        if (weather.wind != null) {
            Wind wind = weather.wind;
            if(wind.speedMPS != null) windSpeed = wind.speedMPS;
            if(wind.direction != null) windDir = wind.direction;
        }
        if(weather.temperature != null) temperature = weather.temperature;

        return new Loadable<>(new HomeViewModel(flagStatus, temperature, windSpeed, windDir));
    }


}
