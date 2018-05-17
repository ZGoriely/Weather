package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingSchedulers;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.RowingInfoSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
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
                .throttleFirst(6, TimeUnit.SECONDS)
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

        return new Loadable<>(new HomeViewModel(flagText, weatherText));
    }


}
