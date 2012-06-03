package org.nolat.best.tasks.screenshot;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JRootPane;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class ScreenshotTask extends JFrame {

    private static final Logger log = Logger.getLogger(ScreenshotTask.class);

    public ScreenshotTask() {
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setBounds(getFinalSize());
        setVisible(true);

        ScreenshotPanel sp = new ScreenshotPanel(getFinalSize(), this);
        this.add(sp);
    }

    private Rectangle getFinalSize() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = ge.getScreenDevices();
        Rectangle bounds = new Rectangle();

        for (GraphicsDevice device : devices) {

            GraphicsConfiguration[] gc = device.getConfigurations();

            for (int i = 0; i < gc.length; i++) {
                bounds = bounds.union(gc[i].getBounds());
            }
        }

        log.info("Rectangle bounds: " + bounds);
        return bounds;
    }
}
