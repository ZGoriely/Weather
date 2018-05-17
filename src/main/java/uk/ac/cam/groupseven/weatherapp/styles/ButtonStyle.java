package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ButtonStyle extends BackgroundStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        super.styleComponent(component);
        component.setBorder(new LineBorder(Color.WHITE, 1));
    }
}
