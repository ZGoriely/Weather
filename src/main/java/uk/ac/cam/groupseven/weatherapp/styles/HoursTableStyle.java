package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class HoursTableStyle extends BackgroundStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        // Table style, set heading and cell styles
        JTable jTable = (JTable) component;
        super.styleComponent(jTable.getTableHeader());
        jTable.getTableHeader().setBorder(new LineBorder(Color.WHITE, 1));
        Font font = new Font("Helvetica", Font.PLAIN, 30);
        jTable.setFont(font);
        Font headingFont = new Font("Helvetica", Font.BOLD, 30);
        jTable.getTableHeader().setFont(headingFont);
        jTable.setRowHeight(100);
        jTable.setFillsViewportHeight(true);
        jTable.setShowGrid(false);
        super.styleComponent(((JComponent) jTable.getParent()));

    }
}

