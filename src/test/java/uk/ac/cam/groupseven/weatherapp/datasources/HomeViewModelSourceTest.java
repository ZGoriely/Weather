package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.reactivex.Observable;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.cam.groupseven.weatherapp.mocking.MockedModule;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.HomeViewModelSource;

public class HomeViewModelSourceTest {

    @Test
    public void getViewModel() {
        Loadable<HomeViewModel> homeViewModel = MockedModule.injector.getInstance(HomeViewModelSource.class)
                .getViewModel(Observable.just(new Object()))
                .lastOrError()
                .blockingGet();


        Assert.assertEquals(MockedModule.injector.getInstance(new Key<Loadable<HomeViewModel>>(){}), homeViewModel);

    }

}