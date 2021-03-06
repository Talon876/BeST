package org.nolat.best.tasks.screenshot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.nolat.best.preferences.Prefs;
import org.nolat.best.tasks.net.ImgurUpload;

@SuppressWarnings("serial")
public class ScreenshotPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    private static final Logger log = Logger.getLogger(ScreenshotPanel.class);

    private final Rectangle bounds;
    private BufferedImage background;
    private int mx, my;
    private Point startPoint = new Point();
    private Point endPoint = new Point();
    private final Rectangle2D selection;
    private final ScreenshotTask parent;
    private static final String[] HELP_TEXT = { "Click and drag to select.", "Press <enter> to upload.",
        "Press <esc> to cancel.", "<c> toggles crosshair.", "<F1> toggles this help." };

    public ScreenshotPanel(Rectangle bounds, ScreenshotTask parent) {
        this.bounds = bounds;
        this.parent = parent;
        try {
            Robot robot = new Robot();
            background = robot.createScreenCapture(bounds);
        } catch (AWTException e) {
            log.error(e);
        }
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        updateCursor();
        selection = new Rectangle2D.Double();
    }

    private void updateCursor() {
        if (Prefs.prefs.at("screenshot.crosshair.enabled").asBoolean()) {
            BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), null);
            setCursor(blankCursor);
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(background, 0, 0, null);
        drawOverlay(g2d);
        drawSelection(g2d);
        drawCursor(g2d);
        drawHelp(g2d);
    }

    private void drawOverlay(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.BLACK);
    }

    private void drawSelection(Graphics2D g2d) {
        selection.setFrameFromDiagonal(startPoint, endPoint);
        Rectangle select = selection.getBounds();
        if (getSelectedImage() != null) {
            g2d.drawImage(getSelectedImage(), select.x, select.y, null);
            g2d.setColor(Color.RED);
            g2d.drawRect(select.x - 1, select.y - 1, select.width + 1, select.height + 1);
            g2d.setColor(Color.BLACK);
        }
    }

    private void drawCursor(Graphics2D g2d) {
        if (Prefs.prefs.at("screenshot.crosshair.enabled").asBoolean()) {
            g2d.setColor(Color.RED);
            g2d.drawLine(0, my, mx - 8, my); //left
            g2d.drawLine(mx, 0, mx, my - 8); //top
            g2d.drawLine(bounds.width, my, mx + 8, my); //right
            g2d.drawLine(mx, bounds.height, mx, my + 8); //bottom

            g2d.setColor(new Color(0, 0, 0, 64));
            g2d.drawLine(mx - 4, my, mx + 4, my); //horizontal
            g2d.drawLine(mx, my - 4, mx, my + 4); //vertical
            g2d.setColor(Color.BLACK);
        }
    }

    private void drawHelp(Graphics2D g2d) {
        if (Prefs.prefs.at("screenshot.help.enabled").asBoolean()) {
            g2d.setColor(new Color(255, 255, 255, 64));
            g2d.fillRoundRect(mx + 3, my + 3, 134, HELP_TEXT.length * 16 - 2, 6, 6);
            g2d.setColor(new Color(32, 32, 32, 192));
            for (int i = 0; i < HELP_TEXT.length; i++) {
                g2d.drawString(HELP_TEXT[i], mx + 5, my + ((i + 1) * 15));
            }
            g2d.setColor(Color.BLACK);
        }
    }

    private BufferedImage getSelectedImage() {
        Rectangle select = selection.getBounds();
        if (select.width > 0 && select.height > 0) {
            return background.getSubimage(select.x, select.y, select.width, select.height);
        } else {
            return null;
        }
    }

    private void clearSelection() {
        startPoint = new Point(0, 0);
        endPoint = new Point(0, 0);
    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {
        startPoint = new Point(mx, my);
        endPoint = new Point(mx, my);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        endPoint = new Point(mx, my);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        mx = me.getX();
        my = me.getY();
        endPoint = new Point(mx, my);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        mx = me.getX();
        my = me.getY();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent ke) {

    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            parent.dispose();
        } else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            if (getSelectedImage() == null) {
                parent.dispose();
            } else {
                new ImgurUpload(getSelectedImage());
                clearSelection();
                parent.dispose();
                //Tray.publishMessage("Your screenshot will be uploaded soon. Probably.", "Uploading...");
            }
        } else if (ke.getKeyCode() == KeyEvent.VK_F1) {
            Prefs.toggleBooleanProperty("screenshot.help.enabled");
        } else if (ke.getKeyCode() == KeyEvent.VK_C) {
            Prefs.toggleBooleanProperty("screenshot.crosshair.enabled");
            updateCursor();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }
}
