package husacct.graphics.presentation;

import java.awt.geom.Point2D;

import org.jhotdraw.draw.Figure;

import husacct.graphics.presentation.figures.ModuleFigure;


public class Drawing extends org.jhotdraw.draw.DefaultDrawing
{
	private static final long serialVersionUID = 3212318618672284266L;
	
	public Drawing()
	{
		super();
	}

	public ModuleFigure getShownModules()
	{
		return null;
	}
	
	@Override 
	public boolean add(Figure f)
	{
		f.setBounds(new Point2D.Double(10, 10), new Point2D.Double(60, 100));
		
		//TODO implement layout mechanism here
		
		return super.add(f);
	}
}
