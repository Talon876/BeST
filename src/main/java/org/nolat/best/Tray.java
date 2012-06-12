package org.nolat.best;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;
import org.nolat.best.preferences.Prefs;
import org.nolat.best.tasks.TaskManager;

public class Tray implements ActionListener {

    private static final Logger log = Logger.getLogger(Tray.class);

    private static JXTrayIcon tray;
    private static JPopupMenu popup;

    public Tray() {
        initializeTray();
    }

    private void initializeTray() {
        tray = new JXTrayIcon(IconManager.getIcon("arrow_divide").getImage(), Launcher.getName() + " - "
                + Launcher.getVersion());
        popup = buildPopupMenu();

        tray.setJPopupMenu(popup);
        tray.addActionListener(this);
        tray.setActionCommand("tray");

        SystemTray systemTray = SystemTray.getSystemTray();
        try {
            log.debug("Adding to tray");
            systemTray.add(tray);
        } catch (AWTException e) {
            log.error(e);
        }
        if (Prefs.prefs.at("app.displaywelcome").asBoolean()) {
            Tray.publishMessage("Rightclick the icon for more information.", "Thanks for trying BeST!");
            Prefs.prefs.set("app.displaywelcome", false);
            Prefs.save();
        }
    }

    private JPopupMenu buildPopupMenu() {
        JPopupMenu popup = new JPopupMenu();

        /* Help -->
         *      About
         *      Instructions
         * -----------------
         * Take Screenshot
         * Save Text
         * Preferences
         * ----
         * Exit
         */

        JMenu helpMenu = new JMenu("Help", true);
        JMenuItem helpAboutMenu = new JMenuItem("About", IconManager.getIcon("help"));
        JMenuItem helpInstructMenu = new JMenuItem("Instructions", IconManager.getIcon("information"));
        helpMenu.add(helpAboutMenu);
        helpMenu.add(helpInstructMenu);
        helpAboutMenu.addActionListener(this);
        helpInstructMenu.addActionListener(this);
        helpAboutMenu.setActionCommand("about");
        helpInstructMenu.setActionCommand("instructions");

        JMenuItem screenshotMenu = new JMenuItem("Take Screenshot", IconManager.getIcon("camera_link"));
        screenshotMenu.addActionListener(this);
        screenshotMenu.setActionCommand("screenshot");

        //        JMenuItem saveTextMenu = new JMenuItem("Save Text to Pastebin", IconManager.getIcon("page_white_link"));
        //        saveTextMenu.addActionListener(this);
        //        saveTextMenu.setActionCommand("text");

        JMenuItem preferencesMenu = new JMenuItem("Preferences", IconManager.getIcon("cog"));
        preferencesMenu.addActionListener(this);
        preferencesMenu.setActionCommand("preferences");

        JMenuItem toolsMenu = new JMenuItem("Tools", IconManager.getIcon("wrench"));
        toolsMenu.addActionListener(this);
        toolsMenu.setActionCommand("tools");

        JMenuItem exitMenu = new JMenuItem("Exit", IconManager.getIcon("cancel"));
        exitMenu.addActionListener(this);
        exitMenu.setActionCommand("exit");

        popup.add(helpMenu);
        popup.addSeparator();
        popup.add(screenshotMenu);
        //        popup.add(saveTextMenu);
        popup.add(toolsMenu);
        popup.add(preferencesMenu);
        popup.addSeparator();
        popup.add(exitMenu);

        return popup;
    }

    public static void publishMessage(String message) {
        publishMessage(message, "Message", MessageType.NONE);
    }

    public static void publishMessage(String message, String title) {
        publishMessage(message, title, MessageType.NONE);
    }

    public static void publishMessage(String message, String title, MessageType messageType) {
        if (tray != null) {
            log.debug("Displaying message: " + message);
            tray.displayMessage(title, message, messageType);
        } else {
            log.warn("Couldn't display message '" + message + "' because the tray was still null.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug("ActionCommand " + e.getActionCommand() + " activated");

        TaskManager.execute(e.getActionCommand());
    }
}
