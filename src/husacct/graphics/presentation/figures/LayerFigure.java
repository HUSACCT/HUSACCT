package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;

public class LayerFigure extends NamedFigure {	
	private static final long serialVersionUID = 101138923385231941L;
	private RectangleFigure body;
	
	public LayerFigure(String name)
	{		
		super(name);
		
		body = new RectangleFigure();		
		children.add(body);
	}	
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead)
	{
		body.setBounds(anchor, lead);
	}	
	
	@Override
	public LayerFigure clone() {
		
		LayerFigure other = (LayerFigure)super.clone();
		other.body = body.clone();
		
		other.children = new ArrayList<Figure>();
		other.children.add(body);
		
		return other;
	}
}
