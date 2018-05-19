package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import hu.akarnokd.rxjava2.swing.SwingSchedulers;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.OpenWeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.DayWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.DaysViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class DaysViewModelSource implements ViewModelSource<Loadable<DaysViewModel>> {
    @Inject
    @Named("morningHour")
    private Integer morningHour;
    @Inject
    @Named("afternoonHour")
    private Integer afternoonHour;
    @Inject
    private OpenWeatherSource weatherApiSource;

    @Override
    public Observable<Loadable<DaysViewModel>> getViewModel(Observable<Object> refresh) {
        return Observable.range(1, 4)
                .concatMap(x ->
                        weatherApiSource.getWeatherInDays(x, morningHour)
                                .concatMap(morningWeather ->
                                        weatherApiSource.getWeatherInDays(x, afternoonHour)
                                                .map(afternoonWeather ->
                                                        buildModel(morningWeather, afternoonWeather)
                                                )

                                )
                )
                .toList()
                .map(this::buildModel)
                .toObservable()
                .observeOn(SwingSchedulers.edt());
    }

    private Loadable<DaysViewModel> buildModel(List<DayWeather> dayWeathers) {
        return new Loadable<>(new DaysViewModel(dayWeathers));
    }

    private DayWeather buildModel(Weather morningWeather, Weather afternoonWeather) {
        // Set up model text and return model with correct text
        String dateText = morningWeather.getFromTime().format(DateTimeFormatter.ofPattern("dd/MM"));
        String morningTemperature = String.format("%.1f°C", morningWeather.getTemperature());
        String morningWind = String.format("%.1f m/s", morningWeather.getWind().getSpeedMPS());
        String afternoonTemperature = String.format("%.1f°C", afternoonWeather.getTemperature());
        String afternoonWind = String.format("%.1f m/s", afternoonWeather.getWind().getSpeedMPS());
        return new DayWeather(dateText, morningTemperature, morningWind, afternoonTemperature, afternoonWind);
    }
}
