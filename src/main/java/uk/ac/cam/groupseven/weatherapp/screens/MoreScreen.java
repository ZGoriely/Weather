package uk.ac.cam.groupseven.weatherapp.screens;

import com.sun.tools.corba.se.idl.constExpr.Not;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;

import javax.swing.*;

public class MoreScreen implements Screen {
    private JButton upButton;
    private JTextPane WeatherGraphic;
    private JTextPane WeatherInfo;
    private JPanel panel;

    @Override
    public Disposable start() {
        throw new NotImplementedException();
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(upButton).map(x -> ScreenLayout.Direction.UP);
    }

    @Override
    public JPanel getPanel() { return panel; }
}