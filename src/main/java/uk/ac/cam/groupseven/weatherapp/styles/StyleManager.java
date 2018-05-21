package uk.ac.cam.groupseven.weatherapp.styles;

import uk.ac.cam.groupseven.weatherapp.Screen;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

public class StyleManager {

    public static void applyStyles(Screen screen) {
        applyStyles((Object) screen);
    }

    public static void applyStyles(Component component) {
        applyStyles((Object) component);
    }

    //Walk the object tree applying styles
    private static void applyStyles(Object object) {
        //Cache instances of styles
        ConcurrentHashMap<Class<?>, Style> instances = new ConcurrentHashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {

            try {
                //We need access to private fields.
                field.setAccessible(true);
                Object fieldObject = field.get(object);

                //Check for either style annotations
                if (field.isAnnotationPresent(ApplyStyle.class) || field.isAnnotationPresent(ApplyStyles.class)) {
                    if (fieldObject != null) {
                        //Recursively apply styles
                        applyStyles(fieldObject);

                        //Check if the field is a JComponent
                        if (JComponent.class.isAssignableFrom(field.getType())) {
                            JComponent component = (JComponent) fieldObject;

                            //Apply each style
                            for (ApplyStyle applyStyle : field.getAnnotationsByType(ApplyStyle.class)) {
                                for (Class<? extends Style> styleClass : applyStyle.value()) {
                                    //Create new object or retrieve from cache
                                    Style style = instances.computeIfAbsent(styleClass, x -> {
                                        try {
                                            return styleClass.newInstance();
                                        } catch (InstantiationException | IllegalAccessException e) {
                                            e.printStackTrace();
                                            return null;
                                        }
                                    });

                                    //Errors are not fatal
                                    if (style != null) {
                                        style.styleComponent(component);
                                    }
                                }

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

}
