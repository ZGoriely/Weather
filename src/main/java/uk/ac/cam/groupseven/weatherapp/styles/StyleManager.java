package uk.ac.cam.groupseven.weatherapp.styles;

import uk.ac.cam.groupseven.weatherapp.Screen;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

public class StyleManager { // Class that handles the styling of Swing elements

    public static void applyStyles(Screen screen) {
        applyStyles((Object) screen);
    }

    public static void applyStyles(Component component) {
        applyStyles((Object) component);
    }

    private static void applyStyles(Object object) {
        ConcurrentHashMap<Class<?>, Style> instances = new ConcurrentHashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {

            try {
                field.setAccessible(true);
                Object fieldObject = field.get(object);
                if (field.isAnnotationPresent(ApplyStyle.class) || field.isAnnotationPresent(ApplyStyles.class)) {
                    if (fieldObject != null) {
                        applyStyles(fieldObject);
                        if (JComponent.class.isAssignableFrom(field.getType())) {
                            JComponent component = (JComponent) fieldObject;


                            for (ApplyStyle applyStyle : field.getAnnotationsByType(ApplyStyle.class)) {
                                ApplyStyle(instances, component, applyStyle);
                            }
                        } else if (field.isAnnotationPresent(ApplyStyle.class)) {
                            System.out.println(String.format("Warning: Could not style %s as it was not a component", field.toString()));

                        }

                    } else {
                        System.out.println(String.format("Warning: Could not style %s as it was null", field.toString()));
                    }


                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private static void ApplyStyle(ConcurrentHashMap<Class<?>, Style> instances, JComponent component, ApplyStyle applyStyle) {
        for (Class<? extends Style> styleClass : applyStyle.value()) {
            Style style = instances.computeIfAbsent(styleClass, x -> {
                try {
                    return styleClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            if (style != null) {
                style.styleComponent(component);
            }
        }

    }
}
