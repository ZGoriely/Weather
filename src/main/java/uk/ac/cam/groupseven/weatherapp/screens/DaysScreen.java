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
import uk.ac.cam.groupseven.weatherapp.viewmodels.DayWeather;
import uk.ac.cam.groupseven.weatherapp.viewmodels.DaysViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
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
    private JPanel topPanel;
    @ApplyStyle({BackgroundStyle.class, BigTextStyle.class})
    private JLabel dateText;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel bottomPanel;
    @ApplyStyle(ButtonStyle.class)
    private JScrollPane scrollPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel midPanel;

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
                public int getRowCount() {
                    return viewModel.getDayWeathers().size();
                }

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

                @Override
                public boolean isCellEditable(int row, int column) { return false; }

            });

            // Set up column models
            TableColumnModel columnModel = forecastTable.getColumnModel();
            ColumnGroup groupMorning = new ColumnGroup("Morning");
            groupMorning.add(columnModel.getColumn(1));
            groupMorning.add(columnModel.getColumn(2));
            ColumnGroup groupAfternoon = new ColumnGroup("Afternoon");
            groupAfternoon.add(columnModel.getColumn(3));
            groupAfternoon.add(columnModel.getColumn(4));

            // Set up header
            GroupableTableHeader header = new GroupableTableHeader(columnModel);
            header.addColumnGroup(groupMorning);
            header.addColumnGroup(groupAfternoon);
            header.setFont(new Font("Helvetica", Font.BOLD, 30));

            // Set text and background colors
            header.setBackground(new Color(0, 0, 80));
            header.setForeground(new Color(255, 255, 255));
            header.setBorder(new LineBorder(Color.WHITE, 1));
            Font headingFont = new Font("Helvetica", Font.BOLD, 30);
            header.setFont(headingFont);
            forecastTable.setTableHeader(header);

            // Set column header renderer for first column
            forecastTable.getColumnModel().getColumn(0)
                    .setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> {
                        JLabel jLabel = new JLabel((String) value);
                        jLabel.setFont(table.getTableHeader().getFont());
                        jLabel.setForeground(table.getTableHeader().getForeground());
                        return jLabel;
                    });

            //Set column model for all other columns
            for (int col = 1; col < 5; col++) {
                forecastTable.getColumnModel().getColumn(col)
                        .setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> new JLabel((Icon) value));
            }

            // Set column headers
            forecastTable.getColumnModel().getColumn(0).setHeaderValue("Date");
            forecastTable.getColumnModel().getColumn(1).setHeaderValue(scaledTempIcon);
            forecastTable.getColumnModel().getColumn(2).setHeaderValue(scaledWindIcon);
            forecastTable.getColumnModel().getColumn(3).setHeaderValue(scaledTempIcon);
            forecastTable.getColumnModel().getColumn(4).setHeaderValue(scaledWindIcon);

            // Show grid so entries are separated
            forecastTable.setShowGrid(true);
            forecastTable.invalidate();
        }
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
        panel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.setMinimumSize(new Dimension(500, 300));
        panel.setPreferredSize(new Dimension(500, 300));
        panel.setRequestFocusEnabled(false);
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 0, 0, 0), -1, -1));
        panel.add(topPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 200), new Dimension(150, 200), new Dimension(-1, 200), 0, false));
        dateText = new JLabel();
        dateText.setAlignmentX(0.5f);
        dateText.setText("Label");
        topPanel.add(dateText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 1, new Insets(25, 50, 50, 50), -1, -1));
        bottomPanel.setOpaque(false);
        panel.add(bottomPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scrollPanel = new JScrollPane();
        scrollPanel.setOpaque(true);
        bottomPanel.add(scrollPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        forecastTable = new JTable();
        forecastTable.setFillsViewportHeight(true);
        forecastTable.setSelectionBackground(new Color(-16166704));
        forecastTable.setSelectionForeground(new Color(-16777216));
        scrollPanel.setViewportView(forecastTable);
        midPanel = new JPanel();
        midPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 10, 0, 10), -1, -1));
        midPanel.setOpaque(false);
        panel.add(midPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 100), new Dimension(-1, 100), new Dimension(-1, 100), 0, false));
        leftButton = new JButton();
        leftButton.setAlignmentX(0.5f);
        leftButton.setHideActionText(false);
        this.$$$loadButtonText$$$(leftButton, ResourceBundle.getBundle("strings").getString("24.hour1"));
        midPanel.add(leftButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(149, 50), null, 0, false));
        final Spacer spacer1 = new Spacer();
        midPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        rightButton = new JButton();
        rightButton.setAlignmentX(0.5f);
        rightButton.setHideActionText(false);
        this.$$$loadButtonText$$$(rightButton, ResourceBundle.getBundle("strings").getString("current.forecast"));
        midPanel.add(rightButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(136, 50), null, 0, false));
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

