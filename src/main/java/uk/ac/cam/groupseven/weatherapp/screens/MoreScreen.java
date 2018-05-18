package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.models.Crest;
import uk.ac.cam.groupseven.weatherapp.styles.*;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodels.MoreViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class MoreScreen implements Screen {
    @Inject
    private ViewModelSource<Loadable<MoreViewModel>> moreWeatherSource;

    @ApplyStyle(ButtonStyle.class)
    private JButton upButton;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;
    @ApplyStyle(BackgroundStyle.class)
    private JTextPane WeatherGraphic;
    @ApplyStyle(ButtonStyle.class)
    private JTable infoTable;
    private JTextField placeholderTextField;

    @Override
    public Disposable start() {
        return moreWeatherSource.getViewModel(getRefreshObservable()).subscribe(this::updateTable);
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(upButton).map(x -> ScreenLayout.Direction.UP);
    }

    @Override
    public JPanel getPanel() { return panel; }

    private void updateTable(Loadable<MoreViewModel> loadable) {
        // Rows: precipitation, cloud cover, pressure, humidity, wind direction, other


        MoreViewModel viewModel = loadable.getViewModel();


        Object[][] data = new Object[7][2];
        data[0][0] = new ImageIcon(Paths.get("./res/crests").resolve(Crest.PELBY.getCode() + ".gif").toAbsolutePath().toString());
        data[1][0] = new ImageIcon(Paths.get("./res/crests").resolve(Crest.PELBY.getCode() + ".gif").toAbsolutePath().toString());
        data[2][0] = new ImageIcon(Paths.get("./res/crests").resolve(Crest.PELBY.getCode() + ".gif").toAbsolutePath().toString());
        data[3][0] = new ImageIcon(Paths.get("./res/crests").resolve(Crest.PELBY.getCode() + ".gif").toAbsolutePath().toString());
        data[4][0] = new ImageIcon(Paths.get("./res/crests").resolve(Crest.PELBY.getCode() + ".gif").toAbsolutePath().toString());
        data[4][0] = new ImageIcon(Paths.get("./res/icons/").resolve("wind.png").toAbsolutePath().toString());
        data[5][0] = new ImageIcon(Paths.get("./res/crests").resolve(Crest.PELBY.getCode() + ".gif").toAbsolutePath().toString());
        data[6][0] = new ImageIcon(Paths.get("./res/crests").resolve(Crest.PELBY.getCode() + ".gif").toAbsolutePath().toString());

        if (loadable.getError() != null) {
            for (int i = 0; i < data.length; i++) {
                data[i][1] = "error";
            }
            return;
        } else if (loadable.getLoading()) {
            for (int i = 0; i < data.length; i++) {
                data[i][1] = "loading...";
            }
            return;
        }

        data[0][1] = viewModel.getWaterLevel().level + " metres";
        data[1][1] = viewModel.getWeather().precipitation.toString();
        data[2][1] = viewModel.getWeather().cloudCover + "%";
        data[3][1] = viewModel.getWeather().temperature + " C";
        data[4][1] = viewModel.getWeather().wind.direction;
        data[4][1] = viewModel.getWeather().wind.speedMPS + " m/s";
        data[5][1] = viewModel.getLightingLimes().todayUpTime;
        data[6][1] = viewModel.getLightingLimes().todayDownTime;


        TableModel model = new DefaultTableModel() {

            public int getColumnCount() {
                return data[0].length;
            }

            public int getRowCount() {
                return data.length;
            }

            @Override
            public Class getColumnClass(int columnIndex) {
                return getValueAt(0, columnIndex).getClass();
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (row < data.length && column < data[row].length) {
                    return data[row][column];
                }
                return null;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        infoTable.setModel(model);
        infoTable.setRowHeight((new ImageIcon(Paths.get("./res/crests").resolve(Crest.PELBY.getCode() + ".gif").toAbsolutePath().toString()).getIconHeight()));


    }

    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }
}
