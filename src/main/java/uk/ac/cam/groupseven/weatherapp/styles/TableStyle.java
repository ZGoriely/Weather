package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;

public class TableStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        JTable jTable = (JTable) component;
        jTable.setTableHeader(null);
    }
}
