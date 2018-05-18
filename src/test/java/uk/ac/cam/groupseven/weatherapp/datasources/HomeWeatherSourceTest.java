package uk.ac.cam.groupseven.weatherapp.datasources;

import io.reactivex.Observable;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.mocking.MockedModule;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.HomeViewModelSource;

public class HomeWeatherSourceTest {

    @Test
    public void getViewModel() {
        HomeWeather homeWeather = MockedModule.injector.getInstance(HomeViewModelSource.class)
                .getViewModel(Observable.just(new Object()))
                .lastOrError()
                .blockingGet();

        Assert.assertEquals(MockedModule.injector.getInstance(HomeWeather.class), homeWeather);

    }

}