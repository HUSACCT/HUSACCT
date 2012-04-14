package husacct.graphics.presentation.jhotdraw.figures;

import husacct.graphics.task.figures.AbstractFigure;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jhotdraw.draw.AbstractAttributedCompositeFigure;
import org.jhotdraw.draw.AttributeKeys;

public class AbstractJHotDrawFigure extends AbstractAttributedCompositeFigure
{
	private AbstractFigure taskFigure;
	private static final long serialVersionUID = 3251056850404705883L;
	
	public AbstractJHotDrawFigure(AbstractFigure figure)
	{
		super();
		
		this.taskFigure = figure;
	}
	
	public void setBackgroundColor(Color newColor)
	{
		set(AttributeKeys.FILL_COLOR, newColor);
	}
	
	public void setLineColor(Color newColor)
	{
		set(AttributeKeys.STROKE_COLOR, newColor);
	}
	
	public void setLineThickness(double thickness) 
	{
		set(AttributeKeys.STROKE_WIDTH, thickness);
	}
	
	public void draw(Graphics2D g)
	{
		this.setBackgroundColor(new Color(254, 255, 210));
	
		super.draw(g);
	}
	
	public AbstractFigure getFigure()
	{
		return this.taskFigure;
	}

	@Override
	protected void drawFill(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void drawStroke(Graphics2D g) {
		// TODO Auto-generated method stub

	}
}
