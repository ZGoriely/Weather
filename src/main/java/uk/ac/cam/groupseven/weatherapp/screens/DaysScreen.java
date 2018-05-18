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
import uk.ac.cam.groupseven.weatherapp.styles.ButtonStyle;
import uk.ac.cam.groupseven.weatherapp.styles.TableStyle;
import uk.ac.cam.groupseven.weatherapp.viewmodels.DaysViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class DaysScreen implements Screen {
    @Inject
    ViewModelSource<Loadable<DaysViewModel>> daysWeatherSource;
    @ApplyStyle(ButtonStyle.class)
    private JButton leftButton;
    @ApplyStyle(ButtonStyle.class)
    private JButton rightButton;
    @ApplyStyle(BackgroundStyle.class)
    private JTextPane dateText;
    @ApplyStyle({TableStyle.class, BackgroundStyle.class})
    private JTable forecastTable;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;
    @ApplyStyle(BackgroundStyle.class)
    private JScrollPane tableScroll;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel midPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel topPanel;

    @Override
    public Disposable start() {
        return EmptyDisposable.INSTANCE;
    }

    private void updateScreen(Loadable<DaysViewModel> viewModelLoadable) {
        if (viewModelLoadable.getLoading()) {
            // loading screen
            dateText.setText("loading");
        } else if (viewModelLoadable.getError() != null) {
            // Error screen
            dateText.setText("Error");
        } else {
            // Display screen
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime now = LocalDateTime.now();
            dateText.setText(dtf.format((now)));
            DaysViewModel viewModel = viewModelLoadable.getViewModel();
            updateTable(viewModel);
        }
    }

    private void updateTable(DaysViewModel viewModel) {

        // DefaultTableModel tableModel = (DefaultTableModel) forecastTable.getModel();
        DefaultTableModel tableData = new DefaultTableModel();

        // TODO: Update to properly use viewModel
        String[] columnNames = {"Date",
                "Temperature",
                "Wind speed"};

        for (String column : columnNames) {
            tableData.addColumn(column);
        }

        for (int i = 0; i< viewModel.getTimes().size(); i++) { /* Hi Matt - I think I put the data in the table for you but obviously change if wrong */
            Object[] row = new Object[3];
            row[0] = viewModel.getTimes().get(i).toString(); // Add time as String (might want to do this differently to get a more human-readable time in future)
            row[1] = viewModel.getTemperatures().get(i); // Temperature and wind speed are just added as Float objects. Could string convert them here if needed.
            row[2] = viewModel.getWindSpeeds().get(i);
            tableData.addRow(row);
        }

        forecastTable.setModel(tableData);

        tableData.fireTableDataChanged();
    }

    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .concatWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT)
                .mergeWith(SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT));
    }

    @Override
    public JPanel getPanel() { return panel; }
}

