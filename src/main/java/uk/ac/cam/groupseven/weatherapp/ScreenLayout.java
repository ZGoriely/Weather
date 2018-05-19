package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.cam.groupseven.weatherapp.screens.*;
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
    @ApplyStyles
    @Inject
    MoreScreen moreScreen;

    public JPanel getDefault() { return homeScreen.getPanel(); }

    public JPanel getScreen(Screen current, Direction direction) {
        JPanel currentPanel = current.getPanel();
        JPanel nextPanel = null;

        // Return the appropriate panel to switch to given the current panel
        // Defaults to returning the current panel if the direction was unexpected
        if (currentPanel == homeScreen.getPanel()) {
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
                case DOWN:
                    nextPanel = moreScreen.getPanel();
                    break;
                default:
                    nextPanel = currentPanel;
                    break;
            }
        }
        if (currentPanel == hoursScreen.getPanel()) {
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
        }
        if (currentPanel == crestScreen.getPanel()) {
            return homeScreen.getPanel();
        }
        if (currentPanel == daysScreen.getPanel()) {
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
        }
        if (currentPanel == moreScreen.getPanel()) {
            switch (direction) {
                case UP:
                    nextPanel = homeScreen.getPanel();
                    break;
                default:
                    nextPanel = currentPanel;
                    break;
            }
        }

        // If nextPanel has not been set, that means we've pressed a button on a panel
        // that hasn't been implemented yet; throw exception
        if (nextPanel != null) {
            return nextPanel;
        } else {
            throw new NotImplementedException();
        }
    }

    public Observable<ScreenChange> getScreenChanges() {
        // Map the correct action to each of the buttons
        // Use two merges as one merge can merge a maximum of 4 things
        return Observable.merge(
                Observable.merge(homeScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(homeScreen, x), x)),
                hoursScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(hoursScreen, x), x)),
                crestScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(crestScreen, x), x)),
                daysScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(daysScreen, x), x))), // inner merge ends
                moreScreen.getScreenChanges().map(x -> new ScreenChange(getScreen(moreScreen, x), x))
        );
    }

    public Disposable start() {
        return new CompositeDisposable(
                homeScreen.start(),
                hoursScreen.start(),
                crestScreen.start(),
                daysScreen.start(),
                moreScreen.start()
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
