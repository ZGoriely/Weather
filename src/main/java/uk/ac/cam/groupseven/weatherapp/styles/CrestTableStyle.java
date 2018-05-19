package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;

public class CrestTableStyle extends BackgroundStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        super.styleComponent(((JComponent) component.getParent()));
    }
}
