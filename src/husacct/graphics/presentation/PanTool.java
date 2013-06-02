package husacct.graphics.presentation;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import org.jhotdraw.draw.tool.AbstractTool;

import common.Logger;

@SuppressWarnings("serial")
public class PanTool extends AbstractTool {
	private Logger log = Logger.getLogger(PanTool.class);
	private final Cursor dc;
	private final Cursor hc = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private final JComponent comp;
	private final JViewport vport;
	private Point startPt = new Point();
	
	public PanTool(JViewport vport, JComponent comp) {
		this.vport = vport;
		this.comp = comp;
		dc = comp.getCursor();
		log.debug("created pan tool");
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		log.debug("mouse dragged");

		Point move = new Point();
		Rectangle rect = new Rectangle();
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
	
	@Override
	public void mousePressed(MouseEvent e) {
		comp.setCursor(hc);
		startPt.setLocation(e.getPoint());
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		comp.setCursor(dc);
	}
}
