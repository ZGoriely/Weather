package uk.ac.cam.groupseven.weatherapp;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import javax.swing.*;

public interface Screen {
    Disposable start();

    Observable<ScreenLayout.Direction> getScreenChanges();

    JPanel getPanel();
}
