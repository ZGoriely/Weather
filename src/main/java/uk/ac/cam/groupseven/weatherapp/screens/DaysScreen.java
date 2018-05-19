package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.styles.*;
import uk.ac.cam.groupseven.weatherapp.viewmodels.DayWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.DaysViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
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
    @ApplyStyle({HoursTableStyle.class, BackgroundStyle.class})
    private JTable forecastTable;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel midPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel topPanel;
    @ApplyStyle({BackgroundStyle.class, BigTextStyle.class})
    private JLabel dateText;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel bottomPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JScrollPane scrollPanel;

    @Inject
    @Named("tempSmallIcon")
    private ImageIcon scaledTempIcon;
    @Inject
    @Named("windSmallIcon")
    private ImageIcon scaledWindIcon;

    @Override
    public Disposable start() {
        return daysWeatherSource.getViewModel(getRefreshObservable()).subscribe(this::updateScreen);
    }

    private void updateScreen(Loadable<DaysViewModel> viewModelLoadable) {

        // Deal with errors when getting the data from the viewmodel
        if (viewModelLoadable.getLoading()) {
            dateText.setText("Loading...");
        } else if (viewModelLoadable.getError() != null) {
            dateText.setText("Error");
        } else {
            // Set up useful variables
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime timeNow = LocalDateTime.now();
            dateText.setText(formatter.format((timeNow)));

            DaysViewModel viewModel = viewModelLoadable.getViewModel();

            // Set the table model to get the correct data
            forecastTable.setModel(new DefaultTableModel() {
                @Override
                public int getRowCount() { return viewModel.getDayWeathers().size(); }

                @Override
                public int getColumnCount() {
                    return 5;
                }

                @Override
                public Object getValueAt(int row, int column) {
                    DayWeather dayWeather = viewModel.getDayWeathers().get(row);

                    // Return the correct data depending on which column was passed in
                    switch (column) {
                        case 0:
                            return dayWeather.getDate();
                        case 1:
                            return dayWeather.getMorningTemperature();
                        case 2:
                            return dayWeather.getMorningWind();
                        case 3:
                            return dayWeather.getAfternoonTemperature();
                        case 4:
                            return dayWeather.getAfternoonWind();
                        default:
                            return null;
                    }
                }
            });

            // Set up column models
            TableColumnModel columnModel = forecastTable.getColumnModel();
            ColumnGroup groupMorning = new ColumnGroup("Morning");
            groupMorning.add(columnModel.getColumn(1));
            groupMorning.add(columnModel.getColumn(2));
            ColumnGroup groupAfternoon = new ColumnGroup("Afternoon");
            groupAfternoon.add(columnModel.getColumn(3));
            groupAfternoon.add(columnModel.getColumn(4));

            GroupableTableHeader header = new GroupableTableHeader(columnModel);
            header.addColumnGroup(groupMorning);
            header.addColumnGroup(groupAfternoon);

            // Set text and background colors
            header.setFont(new Font("Helvetica", Font.BOLD, 30));
            header.setBackground(new Color(0, 0, 80));
            header.setForeground(new Color(255, 255, 255));
            header.setBorder(new EtchedBorder());

            forecastTable.setTableHeader(header);

            // Set column header renderer for first column
            forecastTable.getColumnModel().getColumn(0)
                    .setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> {
                        JLabel jLabel = new JLabel((String) value);
                        jLabel.setFont(table.getTableHeader().getFont());
                        jLabel.setForeground(table.getTableHeader().getForeground());
                        return jLabel;
                    });

            // Set column model for all other columns
            forecastTable.getColumnModel().getColumn(0).setHeaderValue("Date");
            for (int col=1; col<5; col++) {
                forecastTable.getColumnModel().getColumn(col)
                        .setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> new JLabel((Icon) value));
            }

            // Set column headers
            forecastTable.getColumnModel().getColumn(1).setHeaderValue(scaledTempIcon);
            forecastTable.getColumnModel().getColumn(2).setHeaderValue(scaledWindIcon);
            forecastTable.getColumnModel().getColumn(3).setHeaderValue(scaledTempIcon);
            forecastTable.getColumnModel().getColumn(4).setHeaderValue(scaledWindIcon);

            // Show grid so entries are separate
            forecastTable.setShowGrid(true);
        }
    }


    private Observable<Object> getRefreshObservable() {
        return Observable.just(new Object()) // Refresh immediately
                        .concatWith(Observable.interval(15, TimeUnit.SECONDS)); // And then refresh every 15 seconds
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        // Map the correct action to each of the buttons
        return SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT)
                .mergeWith(SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT));
    }

    @Override
    public JPanel getPanel() { return panel; }
}

