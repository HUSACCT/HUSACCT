package husacct.control.presentation.util;

import java.awt.Point;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class InternalFrameUtils {

    public static void alignCenter(JInternalFrame internalFrame) {

        JDesktopPane desktopPane = internalFrame.getDesktopPane();

        int desktopWidth = desktopPane.getWidth();
        int desktopHeight = desktopPane.getHeight();
        int frameWidth = internalFrame.getWidth();
        int frameHeight = internalFrame.getHeight();

        Point newLocation = new Point((desktopWidth - frameWidth) / 2, (desktopHeight - frameHeight) / 2);

        internalFrame.setLocation(newLocation);
    }
}
