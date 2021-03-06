package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.datasources.CrestSource;
import uk.ac.cam.groupseven.weatherapp.models.Crest;
import uk.ac.cam.groupseven.weatherapp.styles.*;
import uk.ac.cam.groupseven.weatherapp.viewmodels.CrestViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class CrestScreen implements Screen {

    private final int tableWidth = 4;
    @Inject
    private CrestSource crestSource;
    @Inject
    private ViewModelSource<Loadable<CrestViewModel>> crestViewModelSource;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;
    @ApplyStyle(ButtonStyle.class)
    private JButton returnHomeButton;
    @ApplyStyle({TableStyle.class, CrestTableStyle.class})
    private JTable crestTable;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel bottomPanel;
    private LinkedList<String> crestLabels;

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public Disposable start() {
        return crestViewModelSource.getViewModel(getRefreshObservable()).subscribe(this::updateScreen);
    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        // Map the correct action to each of the buttons
        return SwingObservable.actions(returnHomeButton).map(x -> ScreenLayout.Direction.DOWN).mergeWith(
                SwingObservable.listSelection(crestTable.getSelectionModel())
                        .filter(x -> crestTable.getSelectedRow() >= 0)
                        .filter(x -> crestTable.getSelectedColumn() >= 0)
                        .map(x -> getPos(crestTable.getSelectedRow(), crestTable.getSelectedColumn()))
                        .doOnNext(x -> crestTable.clearSelection())
                        .map(x -> Crest.getCrestFromCode(crestLabels.get(x)))
                        .doOnNext(x -> crestSource.setNewCrest(x))
                        .map(x -> ScreenLayout.Direction.DOWN)
        );
    }

    private void updateScreen(Loadable<CrestViewModel> viewModelLoadable) {
        CrestViewModel viewModel = viewModelLoadable.getViewModel();

        // Set up a map for the crest images
        TreeMap<String, ImageIcon> crests = viewModel.getImages();
        int numCrests = crests.size();

        // Set up a list of crest labels and sort so that crests are displayed alphabetically
        crestLabels = new LinkedList<>(crests.keySet());
        Collections.sort(crestLabels);

        int width = tableWidth;
        int height = (numCrests + width - 1) / width;

        // Set up table data
        Object[][] data = new Object[width][height];
        for (int i = 0; i < numCrests; i++) {
            data[i % width][i / width] = crests.get(crestLabels.get(i));
        }

        // Create new TableModel to get the correct table data and have the correct column class
        // Column class is important in order to display image icons
        TableModel dataModel = new DefaultTableModel() {
            public int getColumnCount() {
                return width;
            }

            public int getRowCount() {
                return height;
            }

            @Override
            public Object getValueAt(int row, int col) {
                int pos = getPos(row, col);
                if (pos >= numCrests) return null;
                return crests.get(crestLabels.get(pos));
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 20;
            }

            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };

        // Set table model
        crestTable.setModel(dataModel);

        // Set table properties to make it look good
        crestTable.setPreferredScrollableViewportSize(crestTable.getPreferredSize());
        crestTable.setRowHeight(crests.firstEntry().getValue().getIconHeight());
    }

    // Private method to convert from a row and column to a list index
    private int getPos(int row, int col) {
        return col + row * tableWidth;
    }

    private Observable<Object> getRefreshObservable() {
        return Observable.just(new Object());
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
        panel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.setMinimumSize(new Dimension(500, 93));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(bottomPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 50), null, 0, false));
        returnHomeButton = new JButton();
        this.$$$loadButtonText$$$(returnHomeButton, ResourceBundle.getBundle("strings").getString("return.home"));
        bottomPanel.add(returnHomeButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 50), null, 0, false));
        final Spacer spacer1 = new Spacer();
        bottomPanel.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        bottomPanel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setOpaque(false);
        panel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        crestTable = new JTable();
        scrollPane1.setViewportView(crestTable);
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

