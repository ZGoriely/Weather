package uk.ac.cam.groupseven.weatherapp.mocking;

import com.google.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;

import javax.swing.*;

public class ScreenLayoutMock extends ScreenLayout {

    @Inject
    Screen screen1;
    @Inject
    Screen screen2;
    @Inject
    Screen screen3;
    @Inject
    Screen screen4;


    @Override
    public JPanel getDefault() {
        return screen1.getPanel();
    }

    @Override
    public JPanel getScreen(Screen current, Direction direction) {
        if (current == screen1) {
            if (direction == Direction.LEFT || direction == Direction.RIGHT)
                return screen2.getPanel();
            else
                return screen3.getPanel();
        }
        if (current == screen2) {
            if (direction == Direction.LEFT || direction == Direction.RIGHT)
                return screen1.getPanel();
            else
                return screen4.getPanel();
        }
        if (current == screen3) {
            if (direction == Direction.LEFT || direction == Direction.RIGHT)
                return screen4.getPanel();
            else
                return screen1.getPanel();
        }
        if (current == screen4) {
            if (direction == Direction.LEFT || direction == Direction.RIGHT)
                return screen3.getPanel();
            else
                return screen2.getPanel();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Observable<ScreenChange> getScreenChanges() {
        return Observable.merge(
                screen1.getScreenChanges().map(x -> new ScreenChange(getScreen(screen1, x), x)),
                screen2.getScreenChanges().map(x -> new ScreenChange(getScreen(screen2, x), x)),
                screen3.getScreenChanges().map(x -> new ScreenChange(getScreen(screen3, x), x)),
                screen4.getScreenChanges().map(x -> new ScreenChange(getScreen(screen4, x), x))
        );
    }

    @Override
    public Disposable start() {
        return new CompositeDisposable(
                screen1.start(),
                screen2.start(),
                screen3.start(),
                screen4.start()

        );
    }
}
