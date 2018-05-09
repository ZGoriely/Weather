package uk.ac.cam.groupseven.weatherapp.datasources;

import io.reactivex.Observable;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.mocking.MockedModule;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeWeather;

public class HomeWeatherSourceTest {

    @Test
    public void getViewModel() throws Exception {
        HomeWeather homeWeather = MockedModule.injector.getInstance(HomeWeatherSource.class)
                .getViewModel(Observable.just(new Object()))
                .lastOrError()
                .blockingGet();

        Assert.assertEquals(MockedModule.injector.getInstance(HomeWeather.class), homeWeather);

    }

}