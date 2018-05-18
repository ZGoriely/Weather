package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;
import java.awt.*;

public class BigTextStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        Font font = new Font("Helvetica", Font.BOLD, 100);
        component.setFont(font);
    }
}