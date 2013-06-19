package husacct.graphics.presentation;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JViewport;

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
	private Point move = new Point();
	private Rectangle rect = new Rectangle();
	
	public PanTool(JViewport vport, JComponent comp) {
		log.debug("pantool: init");
		
		this.vport = vport;
		this.comp = comp;
		dc = comp.getCursor();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point pt = e.getPoint();
		
		int dx = pt.x - startPt.x;
		int dy = pt.y - startPt.y;
		Rectangle vr = vport.getViewRect();
		vr.setRect(vr.x - dx, vr.y - dy, vr.width, vr.height);
		vport.scrollRectToVisible(vr);
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
