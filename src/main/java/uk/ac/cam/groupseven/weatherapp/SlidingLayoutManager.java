package uk.ac.cam.groupseven.weatherapp;

import java.awt.*;

public class SlidingLayoutManager implements LayoutManager {
    private Dimension dimension;
    private float offset;
    private ScreenLayout.Direction direction;

    public SlidingLayoutManager(Dimension dimension) {
        this.dimension = dimension;
    }


    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return dimension;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return dimension;
    }

    @Override
    public void layoutContainer(Container parent) {
        Component frontComponent = null;
        Component backComponent = null;
        if (parent.getComponentCount() > 0) frontComponent = parent.getComponent(0);
        if (parent.getComponentCount() > 1) backComponent = parent.getComponent(1);

        if (frontComponent != null) {
            switch (direction) {
                case LEFT:
                    frontComponent.setBounds((int) (offset * parent.getWidth()), 0, parent.getWidth(), parent.getHeight());
                    break;
                case RIGHT:
                    frontComponent.setBounds((int) (-offset * parent.getWidth()), 0, parent.getWidth(), parent.getHeight());
                    break;
                case UP:
                    frontComponent.setBounds(0, (int) (offset * parent.getHeight()), parent.getWidth(), parent.getHeight());
                    break;
                case DOWN:
                    frontComponent.setBounds(0, (int) (-offset * parent.getHeight()), parent.getWidth(), parent.getHeight());
                    break;
                default:
                    frontComponent.setBounds(0, 0, parent.getWidth(), parent.getHeight());
                    break;
            }
            frontComponent.revalidate();
            frontComponent.repaint();
        }
        if (backComponent != null) {
            switch (direction) {
                case LEFT:
                    backComponent.setBounds((int) (-backComponent.getWidth() + offset * parent.getWidth()), 0, parent.getWidth(), parent.getHeight());
                    break;
                case RIGHT:
                    backComponent.setBounds((int) (backComponent.getWidth() - offset * parent.getWidth()), 0, parent.getWidth(), parent.getHeight());
                    break;
                case UP:
                    backComponent.setBounds(0, (int) (-backComponent.getHeight() + offset * parent.getHeight()), parent.getWidth(), parent.getHeight());
                    break;
                case DOWN:
                    backComponent.setBounds(0, (int) (backComponent.getHeight() - offset * parent.getHeight()), parent.getWidth(), parent.getHeight());
                    break;
                default:
                    backComponent.setBounds(0, 0, 0, 0);
                    break;
            }
            backComponent.revalidate();
            backComponent.repaint();
        }
    }

    public void setOffset(float offset, ScreenLayout.Direction direction) {
        this.offset = offset;
        this.direction = direction;
    }
}
