package husacct.graphics.presentation.menubars;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.graphics.util.UserInputListener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ContextMenu extends JPopupMenu {

    private static final long serialVersionUID = -6033808567664371902L;
    protected ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
    private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();
    private JMenuItem zoomIn;
    private JMenuItem zoomOut;
    private JMenuItem hide;
    private JMenuItem restore;
    private boolean hasSelection = false;
    private boolean hasHiddenFigures = false;
    private boolean canZoomout = false;

    public ContextMenu() {

        ImageIcon icon;

        icon = new ImageIcon(Resource.get(Resource.ICON_ZOOM));
        zoomIn = new JMenuItem(localeService.getTranslatedString("ZoomIn"), icon);
        add(zoomIn);

        icon = new ImageIcon(Resource.get(Resource.ICON_BACK));
        zoomOut = new JMenuItem(localeService.getTranslatedString("ZoomOut"), icon);
        add(zoomOut);

        addSeparator();

        icon = new ImageIcon(Resource.get(Resource.ICON_FIGURES_HIDE));
        hide = new JMenuItem(localeService.getTranslatedString("HideModules"), icon);
        add(hide);

        icon = new ImageIcon(Resource.get(Resource.ICON_FIGURES_SHOW));
        restore = new JMenuItem(localeService.getTranslatedString("RestoreHiddenModules"), icon);
        add(restore);

        hookupEventHandlers();
    }

    private void hookupEventHandlers() {
        zoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerZoomIn();
            }
        });

        zoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerZoomOut();
            }
        });

        hide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerHideModules();
            }
        });

        restore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerRestoreModules();
            }
        });
    }

    protected void triggerZoomIn() {
        for (UserInputListener l : listeners) {
            l.moduleZoom();
        }
    }

    protected void triggerRestoreModules() {
        for (UserInputListener l : listeners) {
            l.restoreModules();
        }
    }

    protected void triggerHideModules() {
        for (UserInputListener l : listeners) {
            l.hideModules();
        }
    }

    private void triggerZoomOut() {
        for (UserInputListener l : listeners) {
            l.moduleZoomOut();
        }
    }

    public void addListener(UserInputListener listener) {
        listeners.add(listener);
    }

    public void removeListener(UserInputListener listener) {
        listeners.remove(listener);
    }

    public void setHasSelection(boolean newValue) {
        hasSelection = newValue;
    }

    public void setHasHiddenFigures(boolean newValue) {
        hasHiddenFigures = newValue;
    }

    public void setCanZoomout(boolean newValue) {
        canZoomout = newValue;
    }

    @Override
    public void show(Component invoker, int x, int y) {
        updateMenuItems();
        super.show(invoker, x, y);
    }

    private void updateMenuItems() {
        hide.setEnabled(hasSelection);
        zoomIn.setEnabled(hasSelection);
        restore.setEnabled(hasHiddenFigures);
        zoomOut.setEnabled(canZoomout);
    }
}
