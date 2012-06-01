package org.nolat.best;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

public class Launcher {

    private static final Logger log = Logger.getLogger(Launcher.class);

    public Launcher() {
        JFrame jframe = new JFrame("Launcher");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.setSize(500, 200);
        jframe.setLocationRelativeTo(null);
        log.info("Attempting to set icon");
        Image image = new ImageIcon((getClass().getResource("/icons/BeST_512.png"))).getImage();
        jframe.setIconImage(image);
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new Launcher();
    }
}
