package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.RowingInfoSource;
import uk.ac.cam.groupseven.weatherapp.datasources.WaterLevelSource;
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
    private WaterLevelSource waterLevelSource;

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
                                        .map(Loadable<MoreViewModel>::new)
                                        .onErrorReturn(Loadable::new)
                        )
        );
    }

    private MoreViewModel buildModel(Weather weather, WaterLevel waterLevel, LightingTimes lightingTimes) throws IllegalArgumentException {
        // Set up text and return a new MoreViewModel
        String waterLevelText = " Water level:\n" + waterLevel.getLevel() + " metres";
        String precipitationText;
        if (weather.getPrecipitation() == null) throw new IllegalArgumentException("Water level was null");
        switch (weather.getPrecipitation()) {
            case NONE:
                precipitationText = "No Rain";
                break;
            case RAIN:
                precipitationText = "Raining";
                break;
            case SNOW:
                precipitationText = "Snowing";
                break;
            default:
                precipitationText = "Error";
                break;
        }
        String precipitation = String.format(" Precipitation:\n%s", precipitationText);
        String cloudCover = String.format(" Cloud cover:\n%d%%", weather.getCloudCover());
        String pressure = String.format(" Pressure:\n%.1fhPa", weather.getPressure());
        String humidity = String.format(" Humidity:\n%d%%", weather.getHumidity());
        String windDirection = String.format(" Wind direction:\n%s", weather.getWind().getDirection());
        String sunrise = String.format(" Sunrise:\n%s", lightingTimes.getTodayUpTime());
        String sunset = String.format(" Sunset:\n%s", lightingTimes.getTodayDownTime());

        return new MoreViewModel(
                waterLevelText,
                precipitation,
                cloudCover,
                pressure,
                humidity,
                windDirection,
                sunrise,
                sunset);
    }
}
