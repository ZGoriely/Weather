package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;
import java.awt.*;

public class BackgroundStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        component.setBackground(new Color(0, 0, 80));
        component.setForeground(new Color(255, 255, 255));
    }
}
