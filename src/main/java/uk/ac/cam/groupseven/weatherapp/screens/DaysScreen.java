package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.styles.*;
import uk.ac.cam.groupseven.weatherapp.viewmodels.DaysViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
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
    private JPanel midPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel topPanel;

    @Override
    public Disposable start() {
        return
                daysWeatherSource
                        .getViewModel(getRefreshObservable())
                        .subscribe(this::updateScreen);
    }

    private void updateScreen(Loadable<DaysViewModel> viewModelLoadable) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        dateText.setText(dtf.format((now)));
        DefaultTableModel tableModel = (DefaultTableModel) resetTable();

        if (viewModelLoadable.getLoading()) {
            // loading screen
            tableModel.addRow(Collections.nCopies(5, "Loading").toArray());

        } else if (viewModelLoadable.getError() != null) {
            // Error screen
            tableModel.addRow(Collections.nCopies(5, "Error").toArray());

        } else {
            // Display screen
            DaysViewModel viewModel = viewModelLoadable.getViewModel();
            updateTable(viewModel, tableModel);
        }
        forecastTable.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }

    private void updateTable(DaysViewModel viewModel, DefaultTableModel tableModel) {
        for (int i=0; i<viewModel.getTimes().size(); i++) {
            Object[] row = new Object[5];
            row[0] = viewModel.getTimes().get(i).toString();
            row[1] = viewModel.getTemperatures().get(2*i);
            row[2] = viewModel.getWindSpeeds().get(2*i);
            row[3] = viewModel.getTemperatures().get(2*i+1);
            row[4] = viewModel.getWindSpeeds().get(2*i+1);
            tableModel.addRow(row);
        }
    }

    private TableModel resetTable() {
        DefaultTableModel tableModel = new DefaultTableModel();

        String[] columnNames = {"Date",
                "Temperature",
                "Wind speed",
                "Temperature",
                "Wind speed"};

        for (String col : columnNames) {
            tableModel.addColumn(col);
        }
        forecastTable.setModel(tableModel);

        TableColumnModel cm = forecastTable.getColumnModel();
        ColumnGroup g_morn = new ColumnGroup("Morning");
        g_morn.add(cm.getColumn(1));
        g_morn.add(cm.getColumn(2));
        ColumnGroup g_noon = new ColumnGroup("Afternoon");
        g_noon.add(cm.getColumn(3));
        g_noon.add(cm.getColumn(4));

        GroupableTableHeader header = new GroupableTableHeader(cm);
        header.addColumnGroup(g_morn);
        header.addColumnGroup(g_noon);

        forecastTable.setTableHeader(header);

        return tableModel;
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

