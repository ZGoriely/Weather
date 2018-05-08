package uk.ac.cam.groupseven.weatherapp.screens;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;

import javax.swing.*;

public interface Screen {
    Disposable start();

    Observable<ScreenLayout.Direction> getScreenChanges();

    JPanel getPanel();
}
