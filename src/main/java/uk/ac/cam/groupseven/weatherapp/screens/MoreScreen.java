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

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    @ApplyStyle(TableStyle.class)
    private JTable infoTable;
    @ApplyStyle(ButtonStyle.class)
    private JScrollPane infoPane;
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


        Object[][] data = new Object[8][2];
        int iconSize = 100;
        //Set icons
        try {
            data[0][0] = new ImageIcon(ImageIO.read(new File("res/icons/waterLevel.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
            data[1][0] = new ImageIcon(ImageIO.read(new File("res/icons/precipitation.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
            data[2][0] = new ImageIcon(ImageIO.read(new File("res/icons/clouds.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
            data[3][0] = new ImageIcon(ImageIO.read(new File("res/icons/pressure.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
            data[4][0] = new ImageIcon(ImageIO.read(new File("res/icons/humidity.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
            data[5][0] = new ImageIcon(ImageIO.read(new File("res/icons/wind.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
            data[6][0] = new ImageIcon(ImageIO.read(new File("res/icons/sunrise.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
            data[7][0] = new ImageIcon(ImageIO.read(new File("res/icons/sunset.png")).getScaledInstance(iconSize, iconSize, Image.SCALE_FAST));
        }
        catch (IOException e) {
            System.out.println(e.getStackTrace());
        }

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
        for (int i = 0; i < 8; i++ ) data[i][1] = "";
        String text = "Water level:\n" + viewModel.getWaterLevel().level + " metres";
        data[0][1] = text;
        data[1][1] = "Precipitation:\n" + viewModel.getWeather().precipitation.toString();
        data[2][1] = "Cloud cover:\n" + viewModel.getWeather().cloudCover + "%";
        data[3][1] = "Pressure:\n" + viewModel.getWeather().pressure + "hPa";
        data[4][1] = "Humidity:\n" + viewModel.getWeather().humidity + "%";
        data[5][1] = "Wind direction:\n" + viewModel.getWeather().wind.direction;
        data[6][1] = "Sunrise:\n" + viewModel.getLightingLimes().todayUpTime;
        data[7][1] = "Sunset:\n" + viewModel.getLightingLimes().todayDownTime;


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
        infoTable.setRowHeight(((ImageIcon)data[0][0]).getIconHeight());
        infoTable.setPreferredScrollableViewportSize(infoTable.getPreferredSize());
        infoTable.getColumnModel().getColumn(0).setPreferredWidth(iconSize);
        infoTable.getColumnModel().getColumn(1).setPreferredWidth(3*iconSize);

    }

    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }
}

