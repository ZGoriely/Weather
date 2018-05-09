package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import javax.inject.Named;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class SlidingPanel extends JPanel {
    int offset = 0;
    @Inject
    @Named("screenDimension")
    Dimension screenDimension;
    private ScreenLayout screenLayout;
    private Disposable currentAnimation = null;

    @Inject
    public SlidingPanel(ScreenLayout screenLayout) {
        this.screenLayout = screenLayout;
        add(screenLayout.getDefault());

    }

    public Disposable start() {
        return new CompositeDisposable(
                screenLayout.getScreenChanges().subscribe(x -> slide(x.nextScreen, x.direction)),
                screenLayout.start());
    }

    Component getFrontPanel() {
        if (getComponents().length > 0) {
            return getComponents()[0];
        }
        return null;
    }

    Component getBackPanel() {
        if (getComponents().length > 1) {
            return getComponents()[1];
        }
        return null;
    }

    @Override
    public Dimension getPreferredSize() {
        return screenDimension;
    }

    @Override
    public Dimension getMinimumSize() {

        return screenDimension;
    }

    @Override
    public Dimension getMaximumSize() {
        return screenDimension;
    }

    void updateBounds(ScreenLayout.Direction direction) {
        if (direction == ScreenLayout.Direction.LEFT) {
            getFrontPanel().setBounds(offset, 0, getWidth(), getHeight());
            getBackPanel().setBounds((int) (-getWidth() + offset), 0, (int) getWidth(), (int) getHeight());
        } else if (direction == ScreenLayout.Direction.RIGHT) {
            getFrontPanel().setBounds(-offset, 0, (int) getWidth(), (int) getHeight());
            getBackPanel().setBounds((int) (getWidth() - offset), 0, (int) getWidth(), (int) getHeight());

        }

    }

    void slide(JPanel next, ScreenLayout.Direction direction) {
        if (currentAnimation != null) {
            currentAnimation.dispose();
        }
        if (getBackPanel() != null) {
            remove(getBackPanel());
        }
        add(next);
        next.revalidate();
        next.repaint();


        currentAnimation = Observable.intervalRange(0, 100, 0, 5, TimeUnit.MILLISECONDS).subscribe(x ->
                {
                    offset = (int) (getFrontPanel().getWidth() * x / 100);
                    updateBounds(direction);
                    this.invalidate();
                },
                x -> {
                    throw new RuntimeException(x);
                },
                () -> remove(getFrontPanel())
        );
    }


}
