package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class PackageFigure extends BaseFigure {
	
	private static final long serialVersionUID = 5449552267500654293L;
	
	private RectangleFigure top;
	private RectangleFigure body;
	private TextFigure text;
	
	public PackageFigure(Rectangle2D.Double rect, String name) {
		super(rect, name);
	}
	
	@Override
	protected void initializeComponents() {
		top = new RectangleFigure(anchor.x, anchor.y, 0, 0);
		body = new RectangleFigure(anchor.x, anchor.y, 0, 0);
		text = new TextFigure(name);
		
		children.add(top);
		children.add(body);
		children.add(text);
		
		super.initializeComponents();
	}
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		this.anchor = anchor;
		this.lead = lead;
		
		//super.setBounds(anchor, lead);		
		top.setBounds(anchor, new Point2D.Double(anchor.x + getWidth() * 0.33f, anchor.y + getHeight() * 0.2f));
		
		Rectangle2D.Double topRect = top.getBounds();
		Point2D.Double topLeft, bottomRight;
		
		topLeft = new Point2D.Double(anchor.x, anchor.y + topRect.height);
		bottomRight = new Point2D.Double(anchor.x + getWidth(), anchor.y + getHeight());
		
		body.setBounds(topLeft, bottomRight);
		text.setBounds(topLeft, bottomRight);
	}	
	
	@Override
	public PackageFigure clone() {
		PackageFigure other = (PackageFigure)super.clone();
		
		other.top = top.clone();
		other.body = body.clone();
		other.text = text.clone();
		
		return other;
	}
}
