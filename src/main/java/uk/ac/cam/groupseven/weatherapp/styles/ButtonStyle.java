package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ButtonStyle extends BackgroundStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        // Style for most buttons, a border with standard background and bold font
        super.styleComponent(component);
        component.setBorder(new LineBorder(Color.WHITE, 1));
        Font font = new Font("Helvetica", Font.BOLD, 25);
        component.setFont(font);
    }
}
