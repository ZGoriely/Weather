package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.viewmodels.DaysWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class DaysScreen implements Screen {
    @Inject
    ViewModelSource<DaysWeather> daysWeatherSource;
    private JButton leftButton;
    private JButton rightButton;
    private JTextPane dateText;
    private JTable forecastTable;
    private JPanel panel;
    private JScrollPane tableScroll;

    @Override
    public Disposable start() {
        return EmptyDisposable.INSTANCE;
    }

    private void updateScreen(DaysWeather viewModel) {
        if (viewModel.loading) {
            // Loading screen
            dateText.setText("Loading");
        } else if (viewModel.error != null) {
            // Error screen
            dateText.setText("Error");
        } else {
            // Display screen
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime now = LocalDateTime.now();
            dateText.setText(dtf.format((now)));
            updateTable(viewModel);
        }
    }

    private void updateTable(DaysWeather viewModel) {

        // DefaultTableModel tableModel = (DefaultTableModel) forecastTable.getModel();
        DefaultTableModel tableData = new DefaultTableModel();

        // TODO: Update to properly use viewModel
        String[] columnNames = {"Date",
                "Temperature",
                "Wind speed"};

        for (String column : columnNames) {
            tableData.addColumn(column);
        }

        for (int i=0; i<viewModel.precipitationTexts.size(); i++) {
            tableData.addRow(viewModel.precipitationTexts.get(i).split("[-]"));
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

