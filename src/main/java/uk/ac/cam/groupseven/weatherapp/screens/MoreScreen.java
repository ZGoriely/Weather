package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.styles.ApplyStyle;
import uk.ac.cam.groupseven.weatherapp.styles.BackgroundStyle;
import uk.ac.cam.groupseven.weatherapp.styles.ButtonStyle;
import uk.ac.cam.groupseven.weatherapp.styles.TableStyle;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodels.MoreViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MoreScreen implements Screen {
    @Inject
    private ViewModelSource<Loadable<MoreViewModel>> moreWeatherSource;

    @ApplyStyle(ButtonStyle.class)
    private JButton upButton;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;
    @ApplyStyle(TableStyle.class)
    private JTable infoTable;
    @ApplyStyle(ButtonStyle.class)
    private JScrollPane infoPane;

    @Inject
    @Named("waterIcon")
    private ImageIcon waterIcon;
    @Inject
    @Named("precipitationIcon")
    private ImageIcon precipitationIcon;
    @Inject
    @Named("cloudsIcon")
    private ImageIcon cloudsIcon;
    @Inject
    @Named("pressureIcon")
    private ImageIcon pressureIcon;
    @Inject
    @Named("humidityIcon")
    private ImageIcon humidityIcon;
    @Inject
    @Named("windIcon")
    private ImageIcon windIcon;
    @Inject
    @Named("sunriseIcon")
    private ImageIcon sunriseIcon;
    @Inject
    @Named("sunsetIcon")
    private ImageIcon sunsetIcon;
    @Inject
    @Named("moreScreenIconSize")
    private int iconSize;

    @Override
    public Disposable start() {
        return moreWeatherSource.getViewModel(getRefreshObservable()).subscribe(this::updateTable);
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        // Map the correct action to the home button
        return SwingObservable.actions(upButton).map(x -> ScreenLayout.Direction.UP);
    }

    private void updateTable(Loadable<MoreViewModel> loadable) {
        MoreViewModel viewModel = loadable.getViewModel();

        Object[][] data = new Object[8][2];
        // Set icons
        data[0][0] = waterIcon;
        data[1][0] = precipitationIcon;
        data[2][0] = cloudsIcon;
        data[3][0] = pressureIcon;
        data[4][0] = humidityIcon;
        data[5][0] = windIcon;
        data[6][0] = sunriseIcon;
        data[7][0] = sunsetIcon;

        // Deal with errors when getting the data from viewmodel
        if (loadable.getError() != null) {
            for (int i = 0; i < data.length; i++) {
                data[i][1] = "error";
            }
        } else if (loadable.getLoading()) {
            for (int i = 0; i < data.length; i++) {
                data[i][1] = "loading...";
            }
        } else if (viewModel != null) {
            // Set up the data for the table
            for (int i = 0; i < 8; i++) data[i][1] = "";
            data[0][1] = viewModel.getWaterLevel();
            data[1][1] = viewModel.getPrecipitation();
            data[2][1] = viewModel.getCloudCover();
            data[3][1] = viewModel.getPressure();
            data[4][1] = viewModel.getHumidity();
            data[5][1] = viewModel.getWindDirection();
            data[6][1] = viewModel.getSunrise();
            data[7][1] = viewModel.getSunset();

            // Create a new TableModel to get the correct data and have the correct column class
            // Column Class is important in order to be able to display the icons
            TableModel model = new DefaultTableModel() {

                @Override
                public int getColumnCount() {
                    return data[0].length;
                }

                @Override
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

            // Apply model to table
            infoTable.setModel(model);

            // Change table properties to make it look good
            infoTable.setRowHeight(((ImageIcon) data[0][0]).getIconHeight());
            infoTable.setPreferredScrollableViewportSize(infoTable.getPreferredSize());
            infoTable.getColumnModel().getColumn(0).setPreferredWidth(iconSize);
            infoTable.getColumnModel().getColumn(1).setPreferredWidth(3 * iconSize);
        } else {
            // An illegal state has been reached; throw exception
            throw new IllegalStateException();
        }
    }

    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); // And then refresh every 15 seconds
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(4, 1, new Insets(20, 0, 20, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setOpaque(false);
        panel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        upButton = new JButton();
        this.$$$loadButtonText$$$(upButton, ResourceBundle.getBundle("strings").getString("home"));
        panel1.add(upButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 50), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(20, 50, 0, 50), -1, -1));
        panel2.setOpaque(false);
        panel.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        infoPane = new JScrollPane();
        panel2.add(infoPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        infoTable = new JTable();
        infoPane.setViewportView(infoTable);
        final Spacer spacer3 = new Spacer();
        panel.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel.add(spacer4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}

