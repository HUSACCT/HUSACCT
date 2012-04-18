package husacct.graphics.presentation.figures;

import husacct.common.dto.AbstractDTO;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class PackageFigure extends ModuleFigure
{	
	private static final long serialVersionUID = 5449552267500654293L;
	
	private RectangleFigure top;
	private RectangleFigure body;
	private TextFigure text;
	
	private static final int MIN_WIDTH = 100;
	private static final int MIN_HEIGHT = 70;
	
	public PackageFigure(AbstractDTO moduleDTO)
	{
		super(moduleDTO);
		
		top = new RectangleFigure();
		body = new RectangleFigure();
		text = new TextFigure(this.getName());
		
		children.add(top);
		children.add(body);
		children.add(text);
	}
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead)
	{
		//minimum size
		if((lead.x - anchor.x) < MIN_WIDTH)
		{
			lead.x = anchor.x + MIN_WIDTH;
		}
		if((lead.y - anchor.y) < MIN_HEIGHT)
		{
			lead.y = anchor.y + MIN_HEIGHT;
		}
		
		top.setBounds(anchor, new Point2D.Double(anchor.x + (lead.x - anchor.x) * 0.33f, 
				anchor.y + (lead.y - anchor.y) * 0.2f));
	
		Point2D.Double topLeft = new Point2D.Double(anchor.x, (anchor.y + top.getBounds().height));
		
		body.setBounds(topLeft, lead);
		text.setBounds(topLeft, lead);
		
		this.invalidate();
	}	
	
	@Override
	public PackageFigure clone()
	{
		PackageFigure other = (PackageFigure)super.clone();
		
		other.top = this.top.clone();
		other.body = this.body.clone();
		other.text = this.text.clone();
		
		other.children = new ArrayList<Figure>();
		other.children.add(other.top);
		other.children.add(other.body);
		other.children.add(other.text);
		
		return other;
	}
}
