package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;
import java.awt.*;

public class TableStyle extends ButtonStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        super.styleComponent(component);
        JTable jTable = (JTable) component;
        jTable.setTableHeader(null);
        Font font = new Font("Helvetica", Font.PLAIN, 28);
        component.setFont(font);
    }
}
