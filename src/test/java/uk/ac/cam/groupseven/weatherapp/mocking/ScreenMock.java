package uk.ac.cam.groupseven.weatherapp.mocking;

import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;

import javax.swing.*;

public class ScreenMock implements Screen {
    private static int instanceNumber = 0;
    private final JPanel panel;
    private JButton leftButton = new JButton("Left");
    private JButton rightButton = new JButton("Right");
    private JButton upButton = new JButton("Up");
    private JButton downButton = new JButton("Down");

    public ScreenMock() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JTextArea(String.format("Screen %s", instanceNumber++)));
        panel.add(leftButton);
        panel.add(rightButton);
        panel.add(upButton);
        panel.add(downButton);


    }

    @Override
    public Disposable start() {
        return EmptyDisposable.INSTANCE;
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return Observable.merge(
                SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT),
                SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT),
                SwingObservable.actions(upButton).map(x -> ScreenLayout.Direction.UP),
                SwingObservable.actions(downButton).map(x -> ScreenLayout.Direction.DOWN)
        );
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
