package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingSchedulers;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.styles.ApplyStyles;

import javax.inject.Named;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class SlidingPanel extends JPanel {
    private SlidingLayoutManager slidingLayoutManager;
    private float offset = 0;
    @ApplyStyles
    private ScreenLayout screenLayout;
    private Disposable currentAnimation = null;

    @Inject
    public SlidingPanel(ScreenLayout screenLayout, @Named("screenDimension") Dimension screenDimension) {
        this.screenLayout = screenLayout;
        this.slidingLayoutManager = new SlidingLayoutManager(screenDimension);
        setLayout(slidingLayoutManager);
        add(screenLayout.getDefault());
        revalidate();

    }

    public Disposable start() {
        //Slide when screen layout tells us to
        return new CompositeDisposable(
                screenLayout.getScreenChanges().subscribe(x -> slide(x.nextScreen, x.direction)),
                screenLayout.start());
    }

    //Similar to double buffering. We have a front active panel and a back panel for animations.
    //When animation is done we swap
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

    //Called from animation
    private void updateBounds(ScreenLayout.Direction direction) {
        slidingLayoutManager.setOffset(offset, direction);
        slidingLayoutManager.layoutContainer(this);
    }

    //Do a slide animmation
    private void slide(JPanel next, ScreenLayout.Direction direction) {
        //We are currently animating. Ignore request.
        if (currentAnimation != null && !currentAnimation.isDisposed()) {
            return;
        }

        //Remove any old panel left over from a previous animation
        if (getBackPanel() != null) {
            remove(getBackPanel());
        }
        add(next);

        //Tell swing we've changed stuff so it updates
        next.revalidate();
        next.repaint();


        int count = 50;
        //Do a animation
        currentAnimation = Observable.intervalRange(0, count, 0, 400 / count, TimeUnit.MILLISECONDS)
                .observeOn(SwingSchedulers.edt()) //On edt thread
                .subscribe(x ->
                        {
                            //Calculate offset (between 0.0 and 1.0)
                            offset = x / (float) count;
                            updateBounds(direction);
                            this.invalidate();
                        },
                        x -> {
                            //Throw any exception
                            throw new RuntimeException(x);
                        },
                        () -> {
                            //When we are done we swap the panels
                            if (getFrontPanel() != null) remove(getFrontPanel());
                            updateBounds(null);
                        }
                );
    }


}
