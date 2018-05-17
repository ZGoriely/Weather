package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.RowingInfoSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WaterLevelSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.LightingTimes;
import uk.ac.cam.groupseven.weatherapp.models.WaterLevel;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.MoreWeather;

public class MoreWeatherSource {
    @Inject
    private RowingInfoSource rowingInfoSource;

    @Inject
    private WeatherSource weatherSource;

    @Inject
    private WaterLevelSource waterLevelSource;

    public Observable<MoreWeather> getViewModel(Observable<Object> refresh) {
        return refresh.flatMap(x ->
                Observable
                        .just(MoreWeather.Loading()) //Return loading followed by the actual data
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

    private MoreWeather buildModel(Weather weather, WaterLevel waterLevel, LightingTimes lightingTimes) {
        return new MoreWeather(weather, waterLevel, lightingTimes);
    }
}
