package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.styles.ApplyStyle;
import uk.ac.cam.groupseven.weatherapp.styles.BackgroundStyle;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class HoursScreen implements Screen {
    @Inject
    ViewModelSource<Loadable<HourViewModel>> viewModelSource;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;
    @ApplyStyle(BackgroundStyle.class)
    private JList<Object> list;
    @ApplyStyle(BackgroundStyle.class)
    private JButton leftButton;
    @ApplyStyle(BackgroundStyle.class)
    private JButton rightButton;
    @ApplyStyle(BackgroundStyle.class)
    private JTextPane dateText;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel midPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel topPanel;

    @Override
    public Disposable start() {
        return EmptyDisposable.INSTANCE;
        //return viewModelSource.getViewModel(getRefreshObservable()).subscribe(x -> updateScreen(x));
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT)
                .mergeWith(SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT));
    }

    private void updateScreen(Loadable<HourViewModel> viewModelLoadable) {
        list.setListData(new Object[0]);
        if (viewModelLoadable.getLoading()) {
            //TODO
        } else if (viewModelLoadable.getError() != null) {
            //TODO
        } else  {
            HourViewModel viewModel = viewModelLoadable.getViewModel();
            assert viewModel != null;
            list.setListData(viewModel.getPrecipitationTexts().toArray());
        }

    }

    public JPanel getPanel() {
        return panel;
    }


    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }
}
