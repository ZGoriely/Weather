package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ButtonStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        component.setBackground(new Color(0, 0, 80));
        component.setForeground(new Color(255, 255, 255));
        component.setBorder(new LineBorder(Color.WHITE, 1));
    }
}
