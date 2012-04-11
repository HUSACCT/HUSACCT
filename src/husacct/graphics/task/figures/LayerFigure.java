package husacct.graphics.task.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jhotdraw.draw.RectangleFigure;

public class LayerFigure extends ModuleFigure {
	
	private static final long serialVersionUID = 101138923385231941L;
	private RectangleFigure body;
	
	public LayerFigure(Rectangle2D.Double rect, String name) {
		
		super(rect, name);
	}
	
	@Override
	protected void initializeComponents() {
		body = new RectangleFigure(anchor.x, anchor.y, 0, 0);		
		children.add(body);
		
		super.initializeComponents();
	}	
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		super.setBounds(anchor, lead);
		
		body.setBounds(anchor, lead);
	}		
}
