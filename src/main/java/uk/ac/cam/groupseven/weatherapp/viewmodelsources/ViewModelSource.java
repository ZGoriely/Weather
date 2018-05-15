package uk.ac.cam.groupseven.weatherapp.viewmodelsources;

import io.reactivex.Observable;

public interface ViewModelSource<T> {
    Observable<T> getViewModel(Observable<Object> refresh);
}
