package uk.ac.cam.groupseven.weatherapp;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class LoadingIcon implements Icon {

    private final ImageIcon imageIcon;
    private int rotation;

    @Inject
    public LoadingIcon(@Named("refreshIcon") Path path) {
        imageIcon = new ImageIcon(path.toAbsolutePath().toString());
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        ((Graphics2D) g).rotate(Math.toRadians(rotation), c.getWidth() / 2, c.getHeight() / 2);
        imageIcon.paintIcon(c, g, x, y);
        ((Graphics2D) g).rotate(Math.toRadians(-rotation), c.getWidth() / 2, c.getHeight() / 2);
    }

    @Override
    public int getIconWidth() {
        return imageIcon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return imageIcon.getIconHeight();
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}
