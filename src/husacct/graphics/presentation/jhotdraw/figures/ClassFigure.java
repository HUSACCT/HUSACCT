package husacct.graphics.presentation.jhotdraw.figures;

import husacct.graphics.task.figures.ModuleFigure;

import java.awt.geom.Point2D;

import org.jhotdraw.draw.RectangleFigure;

public class ClassFigure extends JHotDrawModuleFigure {
	
	private static final long serialVersionUID = -468596930534802557L;
	private RectangleFigure top, middle, bottom;
	
	public ClassFigure(ModuleFigure moduleFigure)
	{
		super(moduleFigure);
	}
	
	@Override
	public void initializeComponents() {
		top = new RectangleFigure(anchor.x, anchor.y, 0, 0);
		middle = new RectangleFigure(anchor.x, anchor.y, 0, 0);
		bottom = new RectangleFigure(anchor.x, anchor.y, 0, 0);
		
		children.add(top);
		children.add(middle);
		children.add(bottom);
		
		super.initializeComponents();
	}
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		top.setBounds(anchor, new Point2D.Double(anchor.x + getWidth(), anchor.y + getHeight() * 0.2) );
		
		middle.setBounds(new Point2D.Double(anchor.x, anchor.y + getHeight() * 0.2), 
				new Point2D.Double(anchor.x + getWidth(), anchor.y + getHeight() * 0.6) );
		
		bottom.setBounds(new Point2D.Double(anchor.x, anchor.y + getHeight() * 0.6),
				new Point2D.Double(anchor.x + getWidth(), anchor.y + getHeight() ));
	}
}
