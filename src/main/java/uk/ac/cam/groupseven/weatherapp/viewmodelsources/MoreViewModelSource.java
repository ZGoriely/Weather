package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.RowingInfoSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WaterLevelSourceImpl;
import uk.ac.cam.groupseven.weatherapp.datasources.WeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.LightingTimes;
import uk.ac.cam.groupseven.weatherapp.models.WaterLevel;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodels.MoreViewModel;

public class MoreViewModelSource implements ViewModelSource<Loadable<MoreViewModel>> {
    @Inject
    private RowingInfoSource rowingInfoSource;

    @Inject
    private WeatherSource weatherSource;

    @Inject
    private WaterLevelSourceImpl waterLevelSource;

    public Observable<Loadable<MoreViewModel>> getViewModel(Observable<Object> refresh) {
        return refresh.flatMap(x ->
                Observable
                        .just(new Loadable<MoreViewModel>()) //Return loading followed by the actual data
                        .concatWith(
                                rowingInfoSource.getLightingStatus().flatMap(lightingTimes ->
                                        weatherSource.getWeatherNow().flatMap(weather ->
                                                waterLevelSource.getWaterLevelNow().map(waterLevel ->
                                                        buildModel(weather, waterLevel, lightingTimes)
                                                )
                                        )
                                )
                        )
        );
    }

    private Loadable<MoreViewModel> buildModel(Weather weather, WaterLevel waterLevel, LightingTimes lightingTimes) {
        return new Loadable<>(new MoreViewModel(weather, waterLevel, lightingTimes));
    }
}
