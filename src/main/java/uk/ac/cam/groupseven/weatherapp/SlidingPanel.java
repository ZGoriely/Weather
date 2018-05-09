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
    private SlidingLayoutManager slidingLayoutManager;
    private float offset = 0;
    private ScreenLayout screenLayout;
    private Disposable currentAnimation = null;

    @Inject
    public SlidingPanel(ScreenLayout screenLayout, @Named("screenDimension") Dimension screenDimension) {
        this.screenLayout = screenLayout;
        this.slidingLayoutManager = new SlidingLayoutManager(screenDimension);
        add(screenLayout.getDefault());
        setLayout(slidingLayoutManager);
        revalidate();

    }

    public Disposable start() {
        return new CompositeDisposable(
                screenLayout.getScreenChanges().subscribe(x -> slide(x.nextScreen, x.direction)),
                screenLayout.start());
    }

    private Component getFrontPanel() {
        if (getComponents().length > 0) {
            return getComponents()[0];
        }
        return null;
    }

    private Component getBackPanel() {
        if (getComponents().length > 1) {
            return getComponents()[1];
        }
        return null;
    }

    private void updateBounds(ScreenLayout.Direction direction) {
        slidingLayoutManager.setOffset(offset, direction);
        slidingLayoutManager.layoutContainer(this);
    }

    private void slide(JPanel next, ScreenLayout.Direction direction) {
        if (currentAnimation != null) {
            currentAnimation.dispose();
        }
        if (getBackPanel() != null) {
            remove(getBackPanel());
        }
        add(next);
        next.revalidate();
        next.repaint();


        int count = 50;
        currentAnimation = Observable.intervalRange(0, count, 0, 400 / count, TimeUnit.MILLISECONDS).subscribe(x ->
                {
                    offset = x / (float) count;
                    updateBounds(direction);
                    this.invalidate();
                },
                x -> {
                    throw new RuntimeException(x);
                },
                () -> {
                    if (getFrontPanel() != null) remove(getFrontPanel());
                    updateBounds(null);
                }
        );
    }


}
