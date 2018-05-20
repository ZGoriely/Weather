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
import uk.ac.cam.groupseven.weatherapp.styles.*;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HourlyWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class HoursScreen implements Screen {
    @Inject
    ViewModelSource<Loadable<HourViewModel>> viewModelSource;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;
    @ApplyStyle(ButtonStyle.class)
    private JButton leftButton;
    @ApplyStyle(ButtonStyle.class)
    private JButton rightButton;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel topPanel;
    @ApplyStyle({HoursTableStyle.class, BackgroundStyle.class})
    private JTable hoursTable;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel bottomPanel;
    @ApplyStyle({BackgroundStyle.class, BigTextStyle.class})
    private JLabel timeLabel;
    @ApplyStyle(ButtonStyle.class)
    private JScrollPane scrollPanel;
    private JPanel midPanel;

    @Inject
    @Named("tempSmallIcon")
    private ImageIcon scaledTempIcon;
    @Inject
    @Named("windSmallIcon")
    private ImageIcon scaledWindIcon;

    @Override
    public Disposable start() {
        return viewModelSource.getViewModel(getRefreshObservable()).subscribe(this::updateScreen);
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        // Map the correct action to each of the buttons
        return SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT)
                .mergeWith(SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT));
    }

    private void updateScreen(Loadable<HourViewModel> viewModelLoadable) {
        // Deal with errors when getting the data from the viewmodel
        if (viewModelLoadable.getLoading()) {
            timeLabel.setText("Loading...");
        } else if (viewModelLoadable.getError() != null) {
            timeLabel.setText("Error");
        } else {
            HourViewModel viewModel = viewModelLoadable.getViewModel();
            assert viewModel != null;

            // Set the label to have the current time
            timeLabel.setText(viewModel.getCurrentTime().toString());

            // Set the table model to get the correct data
            hoursTable.setModel(new DefaultTableModel() {
                @Override
                public int getRowCount() {
                    return viewModel.getHourlyWeather().size();
                }

                @Override
                public int getColumnCount() {
                    return 3;
                }

                @Override
                public Object getValueAt(int row, int column) {
                    HourlyWeather hourlyWeather = viewModel.getHourlyWeather().get(row);

                    // Make each column display the correct information
                    switch (column) {
                        case 0:
                            return hourlyWeather.getTime();
                        case 1:
                            return hourlyWeather.getTemperature();
                        case 2:
                            return hourlyWeather.getWindSpeed();
                        default:
                            return null;
                    }
                }


            });

            // Make columns display the correct information and render correctly
            hoursTable.getColumnModel().getColumn(0)
                    .setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> {
                        JLabel jLabel = new JLabel((String) value);
                        jLabel.setFont(table.getTableHeader().getFont());
                        jLabel.setForeground(table.getTableHeader().getForeground());
                        return jLabel;
                    });
            hoursTable.getColumnModel().getColumn(1)
                    .setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> new JLabel((Icon) value));
            hoursTable.getColumnModel().getColumn(2)
                    .setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> new JLabel((Icon) value));
            hoursTable.getColumnModel().getColumn(0).setHeaderValue("Time");
            hoursTable.getColumnModel().getColumn(1).setHeaderValue(scaledTempIcon);
            hoursTable.getColumnModel().getColumn(2).setHeaderValue(scaledWindIcon);

            hoursTable.invalidate();
            hoursTable.setShowGrid(true);
        }

    }

    public JPanel getPanel() {
        return panel;
    }

    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) // Refresh immediately
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); // And then refresh every 15 seconds
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
        panel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.setMinimumSize(new Dimension(500, 300));
        panel.setPreferredSize(new Dimension(500, 300));
        panel.setRequestFocusEnabled(false);
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 0, 0, 0), -1, -1));
        panel.add(topPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 200), new Dimension(200, 200), new Dimension(-1, 200), 0, false));
        timeLabel = new JLabel();
        timeLabel.setAlignmentX(0.5f);
        timeLabel.setText("Label");
        topPanel.add(timeLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 1, new Insets(25, 50, 50, 50), -1, -1));
        bottomPanel.setOpaque(false);
        panel.add(bottomPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scrollPanel = new JScrollPane();
        scrollPanel.setOpaque(true);
        bottomPanel.add(scrollPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        hoursTable = new JTable();
        hoursTable.setAlignmentX(0.5f);
        hoursTable.setFillsViewportHeight(true);
        hoursTable.setOpaque(false);
        hoursTable.setVisible(true);
        scrollPanel.setViewportView(hoursTable);
        midPanel = new JPanel();
        midPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 10, 0, 10), -1, -1));
        midPanel.setOpaque(false);
        panel.add(midPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 100), new Dimension(-1, 100), new Dimension(-1, 100), 0, false));
        leftButton = new JButton();
        leftButton.setAlignmentX(0.5f);
        leftButton.setHideActionText(false);
        this.$$$loadButtonText$$$(leftButton, ResourceBundle.getBundle("strings").getString("current.forecast1"));
        midPanel.add(leftButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(149, 50), null, 0, false));
        rightButton = new JButton();
        rightButton.setAlignmentX(0.5f);
        rightButton.setHideActionText(false);
        this.$$$loadButtonText$$$(rightButton, ResourceBundle.getBundle("strings").getString("7.day.forecast"));
        midPanel.add(rightButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(136, 50), null, 0, false));
        final Spacer spacer1 = new Spacer();
        midPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
