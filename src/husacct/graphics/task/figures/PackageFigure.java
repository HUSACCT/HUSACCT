package husacct.graphics.task.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jhotdraw.draw.RectangleFigure;

public class PackageFigure extends ModuleFigure {
	
	private static final long serialVersionUID = 5449552267500654293L;
	
	private RectangleFigure top;
	private RectangleFigure body;
	
	public PackageFigure(Rectangle2D.Double rect, String name) {
		super(rect, name);
	}
	
	@Override
	protected void initializeComponents() {
		top = new RectangleFigure(anchor.x, anchor.y, 0, 0);
		body = new RectangleFigure(anchor.x, anchor.y, 0, 0);
		
		children.add(top);
		children.add(body);
		
		super.initializeComponents();
	}
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		this.anchor = anchor;
		this.lead = lead;
		
		super.setBounds(anchor, lead);
		
		top.setBounds(anchor, new Point2D.Double(anchor.x + getWidth() * 0.33f, anchor.y + getHeight() * 0.2f));
		
		Rectangle2D.Double topRect = top.getBounds();
		body.setBounds(new Point2D.Double(anchor.x, anchor.y + topRect.height),
						new Point2D.Double(anchor.x + getWidth(), anchor.y + getHeight()));
	}	
}
