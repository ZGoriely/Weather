package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.cam.groupseven.weatherapp.screens.HomeScreen;
import uk.ac.cam.groupseven.weatherapp.screens.HoursScreen;

import javax.swing.*;

public class ScreenLayout {
    @Inject
    HomeScreen homeScreen;
    @Inject
    HoursScreen hoursScreen;

    public JPanel getDefault() {
        return homeScreen.getPanel();
    }

    public JPanel getScreen(Screen current, Direction direction) {
        JPanel currentPanel = current.getPanel();
        if (currentPanel == homeScreen.getPanel()) {
            JPanel nextPanel;
            switch (direction) {
                case LEFT:
                    nextPanel = hoursScreen.getPanel();
                    break;
                case RIGHT:
                    nextPanel = hoursScreen.getPanel();
                    break;
                default:
                    nextPanel = currentPanel;
                    break;
            }
            return nextPanel;
        }
        if (currentPanel == hoursScreen.getPanel()) {
            JPanel nextPanel;
            switch (direction) {
                case LEFT:
                    nextPanel = homeScreen.getPanel();
                    break;
                case RIGHT:
                    nextPanel = homeScreen.getPanel();
                    break;
                default:
                    nextPanel = currentPanel;
                    break;
            }
            return nextPanel;
        }
        throw new NotImplementedException();
    }

    public Observable<ScreenChange> getScreenChanges() {
        return Observable.merge(
                homeScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(homeScreen, x), x)),
                hoursScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(hoursScreen, x), x))
        );

    }

    public Disposable start() {
        return new CompositeDisposable(
                homeScreen.start(),
                hoursScreen.start()
        );
    }

    public enum Direction {UP, DOWN, LEFT, RIGHT}

    public class ScreenChange {
        public final JPanel nextScreen;
        public final Direction direction;

        public ScreenChange(JPanel nextScreen, Direction direction) {

            this.nextScreen = nextScreen;
            this.direction = direction;
        }
    }
}
