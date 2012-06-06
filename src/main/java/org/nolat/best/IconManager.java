package org.nolat.best;

import java.util.HashMap;

import javax.swing.ImageIcon;

public class IconManager {
    private static HashMap<String, ImageIcon> map = new HashMap<String, ImageIcon>();

    public static void addIcon(String path) {
        String name = stripExtensionAndPath(path);
        ImageIcon icon = new ImageIcon(IconManager.class.getResource(path));
        map.put(name, icon);
    }

    public static void addIcon(String path, String name) {
        ImageIcon icon = new ImageIcon(IconManager.class.getResource(path));
        map.put(name, icon);
    }

    public static ImageIcon getIcon(String name) {
        return map.get(name);
    }

    public static void loadIcons() {
        addIcon("/icons/arrow_divide.png");
        addIcon("/icons/wrench.png");
        addIcon("/icons/camera_link.png");
        addIcon("/icons/page_white_link.png");
        addIcon("/icons/cancel.png");
        addIcon("/icons/help.png");
        addIcon("/icons/information.png");
    }

    public static String stripExtensionAndPath(String path) {
        String name = "";
        if (!path.contains(".") && !path.contains("/")) {
            return path;
        } else if (path.indexOf('.') > path.indexOf('/')) {
            if (path.contains("/")) {
                name = path.substring(path.lastIndexOf('/'), path.lastIndexOf('.')).replace("/", "");
            } else {
                name = path.substring(0, path.lastIndexOf('.'));
            }
        } else {
            if (path.contains("/")) {
                name = path.substring(path.lastIndexOf('/'), path.length() - 1).replace("/", "");
            } else {
                name = path.substring(0, path.lastIndexOf('.'));
            }
        }

        return name;
    }
}
