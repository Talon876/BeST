package org.nolat.best;

import java.awt.SystemTray;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.nolat.best.preferences.Prefs;
import org.nolat.best.tracking.Track;
import org.pushingpixels.substance.api.skin.SubstanceOfficeBlack2007LookAndFeel;

public class Launcher {

    private static final Logger log = Logger.getLogger(Launcher.class);
    private static final String UNSUPPORTED_ERROR = "Unfortunately, your platform is not supported.\nIf you choose to report this error, please include the following information:\nOS: "
            + System.getProperty("os.name") + "\nArch: " + System.getProperty("os.arch");

    public Launcher() {
        if (!SystemTray.isSupported()) {
            log.error(UNSUPPORTED_ERROR);
            JOptionPane.showMessageDialog(null, UNSUPPORTED_ERROR, "Unsupported Platform", JOptionPane.ERROR_MESSAGE);
        } else {
            makeDirs();
            IconManager.loadIcons();
            Prefs.load();
            if (Prefs.prefs.at("app.displaywelcome").asBoolean()) {
                Track.track(Prefs.getEnvironmentProperties());
            }
            new Tray();
        }
    }

    private void makeDirs() {
        File appHome = new File(System.getProperty("user.home") + "/.BeST/screenshots/");
        appHome.mkdirs();
        appHome = new File(System.getProperty("user.home") + "/.BeST/share/");
        appHome.mkdirs();
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceOfficeBlack2007LookAndFeel());
                } catch (UnsupportedLookAndFeelException e) {
                    log.error(e);
                }
                new Launcher();
            }
        });
    }

    public static String getName() {
        String appname = "";
        final Properties pom = new Properties();
        try {
            pom.load(Launcher.class.getResourceAsStream("/META-INF/maven/org.nolat/BeST/pom.properties"));
            appname = pom.getProperty("artifactId");
        } catch (IOException e) {
            appname = "DEV";
            log.error(e);
        }
        return appname;
    }

    public static String getVersion() {
        String version = "";
        final Properties pom = new Properties();
        try {
            pom.load(Launcher.class.getResourceAsStream("/META-INF/maven/org.nolat/BeST/pom.properties"));
            version = pom.getProperty("version");
        } catch (IOException e) {
            log.error(e);
        }
        return version;
    }
}
