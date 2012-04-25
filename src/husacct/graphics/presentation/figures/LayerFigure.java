package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class LayerFigure extends NamedFigure {	
	private static final long serialVersionUID = 101138923385231941L;
	private RectangleFigure body;
	private TextFigure text;
	
	private final int MIN_WIDTH = 300;
	private final int MIN_HEIGHT = 50;
	
	public LayerFigure(String name)
	{		
		super(name);
		
		body = new RectangleFigure();
		text = new TextFigure(name);
		children.add(body);
		children.add(text);
	}	
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead)
	{
		if((lead.x - anchor.x) < this.MIN_WIDTH)
		{
			lead.x = anchor.x + this.MIN_WIDTH;
		}
		if((lead.y - anchor.y) < this.MIN_HEIGHT)
		{
			lead.y = anchor.y + this.MIN_HEIGHT;
		}
		
		body.setBounds(anchor, lead);
		
		// textbox centralising
		double plusX = (((lead.x-anchor.x)-this.text.getBounds().width)/2);
		double plusY = (((lead.y-anchor.y)-this.text.getBounds().height)/2);
		
		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX;
		textAnchor.y += plusY;
		text.setBounds(textAnchor, null);
		
		this.invalidate();
	}	
	
	@Override
	public LayerFigure clone() {
		
		LayerFigure other = (LayerFigure)super.clone();
		other.body = body.clone();
		other.text = text.clone();
		
		other.children = new ArrayList<Figure>();
		other.children.add(other.body);
		other.children.add(other.text);
		
		return other;
	}
}
