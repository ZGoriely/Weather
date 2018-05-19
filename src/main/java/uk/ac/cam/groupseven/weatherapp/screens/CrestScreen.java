package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
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
import java.util.Collections;
import java.util.LinkedList;
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

}

