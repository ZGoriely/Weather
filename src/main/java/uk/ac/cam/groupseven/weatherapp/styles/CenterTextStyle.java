package uk.ac.cam.groupseven.weatherapp.styles;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class CenterTextStyle extends BackgroundStyle implements Style {
    @Override
    public void styleComponent(JComponent component) {
        super.styleComponent(component);
        JTextPane textPane = (JTextPane)component;
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        Font font = new Font("Helvetica", Font.PLAIN, 28);
        component.setFont(font);
    }
}
