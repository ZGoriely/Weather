package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;
import java.awt.*;

public class BigTextStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        // Style for headings, large helvetica font in bold
        Font font = new Font("Helvetica", Font.BOLD, 130);
        component.setFont(font);
    }
}
