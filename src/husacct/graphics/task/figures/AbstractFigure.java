package husacct.graphics.task.figures;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jhotdraw.draw.AbstractAttributedCompositeFigure;
import org.jhotdraw.draw.AttributeKeys;

public class AbstractFigure extends AbstractAttributedCompositeFigure {

	private static final long serialVersionUID = 3251056850404705883L;
	
	public AbstractFigure()
	{
		super();
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

	@Override
	protected void drawFill(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void drawStroke(Graphics2D g) {
		// TODO Auto-generated method stub

	}
}
