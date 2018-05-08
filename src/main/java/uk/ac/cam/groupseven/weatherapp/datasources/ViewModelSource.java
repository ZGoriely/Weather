package uk.ac.cam.groupseven.weatherapp.datasources;

import io.reactivex.Observable;

public abstract class ViewModelSource<T> {
    public abstract Observable<T> getViewModel(Observable<Object> refresh);
}
