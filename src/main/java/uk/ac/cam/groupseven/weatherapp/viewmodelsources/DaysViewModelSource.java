package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingSchedulers;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.OpenWeatherSource;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.DaysViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;

import java.util.ArrayList;
import java.util.List;


public class DaysViewModelSource implements ViewModelSource<Loadable<DaysViewModel>> {
    @Inject
    private OpenWeatherSource weatherApiSource;

    @Override
    public Observable<Loadable<DaysViewModel>> getViewModel(Observable<Object> refresh) {
        return Observable.range(0, 24)
                .flatMap(x -> weatherApiSource.getWeatherInDays(x, 0)) /* TODO SORT THIS OUT - HAVE ARBITRARILY USED 00:00 AS TIME OF DAY */
                .toList()
                .map(this::buildModel)
                .toObservable()
                .observeOn(SwingSchedulers.edt());
    }

    private Loadable<DaysViewModel> buildModel(List<Weather> weatherList) {
        ArrayList<String> weatherTexts = new ArrayList<>();
        for (int i = 0; i < weatherList.size(); i++) {
            switch (weatherList.get(i).precipitation) {
                case RAIN:
                    weatherTexts.add(String.format("%s:00 - Rain", i));
                    break;
                case SNOW:
                    weatherTexts.add(String.format("%s:00 - Snow", i));
                    break;
                case NONE:
                    weatherTexts.add(String.format("%s:00 - Sun", i));
                    break;
            }
        }
        return new Loadable<>(new DaysViewModel(weatherTexts));

    }
}
