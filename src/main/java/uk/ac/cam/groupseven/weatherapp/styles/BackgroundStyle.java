package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;
import java.awt.*;

public class BackgroundStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        component.setBackground(Color.BLUE);
        component.setForeground(Color.white);
    }
}
