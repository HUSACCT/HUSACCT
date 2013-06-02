package husacct.graphics.presentation;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import common.Logger;

public class DragMoverListener extends MouseInputAdapter implements
MouseMotionListener {
	private Logger log = Logger.getLogger(DragMoverListener.class);
	
	private final Cursor dc;
	private final Cursor hc = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private final Rectangle rect = new Rectangle();
	private final JComponent comp;
	private final JViewport vport;
	private Point startPt = new Point();
	private Point move = new Point();
	
	public DragMoverListener(JViewport vport, JComponent comp) {
		this.vport = vport;
		this.comp = comp;
		dc = comp.getCursor();
		vport.addMouseMotionListener(this);
		vport.addMouseListener(this);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		log.debug("dragged");

		if (e.getButton() == MouseEvent.BUTTON2) {
			Point pt = e.getPoint();
			move.setLocation(pt.x - startPt.x, pt.y - startPt.y);
			startPt.setLocation(pt);
			Rectangle vr = vport.getViewRect();
			int w = vr.width;
			int h = vr.height;
			Point ptZero = SwingUtilities.convertPoint(vport, 0, 0, comp);
			rect.setRect(ptZero.x - move.x, ptZero.y - move.y, w, h);
			comp.scrollRectToVisible(rect);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		log.debug("mousePressed");
		
		if (e.getButton() == MouseEvent.BUTTON2) {
			comp.setCursor(hc);
			startPt.setLocation(e.getPoint());
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON2) comp.setCursor(dc);
	}
}
