package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.datasources.CrestSource;
import uk.ac.cam.groupseven.weatherapp.datasources.ViewModelSource;
import uk.ac.cam.groupseven.weatherapp.models.Crest;
import uk.ac.cam.groupseven.weatherapp.viewmodels.CrestViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;

public class CrestScreen implements Screen {

    private final int tableWidth = 4;
    @Inject
    CrestSource crestSource;
    @Inject
    ViewModelSource<CrestViewModel> crestViewModelSource;
    private JPanel panel;
    private JButton returnHomeButton;
    private JScrollPane crestScrollPanel;
    private JTable crestTable;
    private LinkedList<String> crestLabels;

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public Disposable start() {
        return
                crestViewModelSource
                        .getViewModel(getRefreshObservable())
                        .subscribe(viewModel -> updateScreen(viewModel));


    }

    @Override
    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(returnHomeButton).map(x -> ScreenLayout.Direction.DOWN).mergeWith(
                SwingObservable.listSelection(crestTable.getSelectionModel())
                        .map(x -> 0)//TODO: Map to correct crest
                        .doOnNext(x -> crestTable.clearSelection())
                        .map(x -> Crest.getCrestFromCode(crestLabels.get(x)))
                        .doOnNext(x -> crestSource.setNewCrest(x))
                        .map(x -> ScreenLayout.Direction.DOWN)
        );
    }

    private void updateScreen(CrestViewModel viewModel) {
        TreeMap<String, ImageIcon> crests = viewModel.images;
        int numCrests = crests.size();
        crestLabels = new LinkedList<>(crests.keySet());
        Collections.sort(crestLabels);

        int width = tableWidth;
        int height = (numCrests + width - 1) / width;
        System.out.println(width + " " + height);

        Object[][] data = new Object[width][height];
        for (int i = 0; i < numCrests; i++) {
            System.out.println(i + " " + i % width + " " + i / width);
            data[i % width][i / width] = crests.get(crestLabels.get(i));
        }

        TableModel dataModel = new DefaultTableModel() {
            public int getColumnCount() {
                return width;
            }

            public int getRowCount() {
                return height;
            }

            public Object getValueAt(int row, int col) {
                int pos = col + row * width;
                if (pos >= numCrests) return null;
                return crests.get(crestLabels.get(pos));
            }

            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };


        crestTable.setModel(dataModel);
        crestTable.setPreferredScrollableViewportSize(crestTable.getPreferredSize());
        crestTable.setRowHeight(crests.firstEntry().getValue().getIconHeight());
        /*for (int column = 0; column < crestTable.getColumnCount(); column++) {
            crestTable.getColumn(column).setCellRenderer(new CrestRenderer());
            crestTable.getColumn(column).setCellEditor(new CrestEditor(new JCheckBox()));
        }*/
    }

    private Observable<Object> getRefreshObservable() {
        return Observable.just(new Object());
    }

}

class CrestRenderer extends JButton implements TableCellRenderer {

    public CrestRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class CrestEditor extends DefaultCellEditor {
    protected JButton button;

    private String label;

    private boolean isPushed;

    public CrestEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            //
            //
            JOptionPane.showMessageDialog(button, label + ": Ouch!");
            // System.out.println(label + ": Ouch!");
        }
        isPushed = false;
        return new String(label);
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
