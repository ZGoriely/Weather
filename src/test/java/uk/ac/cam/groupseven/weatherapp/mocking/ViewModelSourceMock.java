package uk.ac.cam.groupseven.weatherapp.mocking;

import com.google.inject.Inject;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.datasources.ViewModelSource;

public class ViewModelSourceMock<T> implements ViewModelSource<T> {
    private T mock;

    public ViewModelSourceMock() {

    }


    @Inject
    public ViewModelSourceMock(T mock) {
        this.mock = mock;
    }

    @Override
    public Observable<T> getViewModel(Observable<Object> refresh) {
        if (mock != null) {
            return Observable.just(mock);
        } else {
            return Observable.error(new Exception());
        }
    }
}
