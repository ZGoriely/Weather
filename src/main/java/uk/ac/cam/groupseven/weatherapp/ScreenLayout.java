package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.cam.groupseven.weatherapp.screens.CrestScreen;
import uk.ac.cam.groupseven.weatherapp.screens.DaysScreen;
import uk.ac.cam.groupseven.weatherapp.screens.HomeScreen;
import uk.ac.cam.groupseven.weatherapp.screens.HoursScreen;
import uk.ac.cam.groupseven.weatherapp.styles.ApplyStyles;

import javax.swing.*;

public class ScreenLayout {
    @ApplyStyles
    @Inject
    HomeScreen homeScreen;
    @ApplyStyles
    @Inject
    HoursScreen hoursScreen;
    @ApplyStyles
    @Inject
    CrestScreen crestScreen;
    @ApplyStyles
    @Inject
    DaysScreen daysScreen;
    // @Inject
    // MoreScreen moreScreen;

    public JPanel getDefault() { return homeScreen.getPanel(); }

    public JPanel getScreen(Screen current, Direction direction) {
        JPanel currentPanel = current.getPanel();
        if (currentPanel == homeScreen.getPanel()) {
            JPanel nextPanel;
            switch (direction) {
                case LEFT:
                    nextPanel = daysScreen.getPanel();
                    break;
                case RIGHT:
                    nextPanel = hoursScreen.getPanel();
                    break;
                case UP:
                    nextPanel = crestScreen.getPanel();
                    break;
                /* TODO: Add when moreScreen implemented
                case DOWN:
                    nextPanel = moreScreen.getPanel();
                    break;
                */
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
                    nextPanel = daysScreen.getPanel();
                    break;
                default:
                    nextPanel = currentPanel;
                    break;
            }
            return nextPanel;
        }
        if (currentPanel == crestScreen.getPanel()) {
            return homeScreen.getPanel();
        }
        if (currentPanel == daysScreen.getPanel()) {
            JPanel nextPanel;
            switch (direction) {
                case LEFT:
                    nextPanel = hoursScreen.getPanel();
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
        /* TODO: Add when moreScreen implemented
        if (currentPanel == moreScreen.getPanel()) {
            JPanel nextPanel;
            switch (direction) {
                case UP:
                    nextPanel = homeScreen.getPanel();
                    break;
                default:
                    nextPanel = currentPanel;
                    break;
            }
            return nextPanel;
        }
        */
        throw new NotImplementedException();
    }

    public Observable<ScreenChange> getScreenChanges() {
        return Observable.merge(homeScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(homeScreen, x), x)),
                hoursScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(hoursScreen, x), x)),
                crestScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(crestScreen, x), x)),
                                daysScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(daysScreen, x), x))
        );
    }

    public Disposable start() {
        return new CompositeDisposable(
                homeScreen.start(),
                hoursScreen.start(),
                crestScreen.start(),
                daysScreen.start()
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
