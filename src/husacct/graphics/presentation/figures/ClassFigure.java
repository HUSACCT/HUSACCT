package husacct.graphics.presentation.figures;

import husacct.common.dto.AbstractDTO;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class ClassFigure extends ModuleFigure {

	private static final long serialVersionUID = -468596930534802557L;
	private RectangleFigure top, middle, bottom;
	private TextFigure text;

	public ClassFigure(AbstractDTO moduleDTO)
	{
		super(moduleDTO);
		
		top = new RectangleFigure();
		middle = new RectangleFigure();
		bottom = new RectangleFigure();
		text = new TextFigure(getName());

		children.add(top);
		children.add(middle);
		children.add(text);
		children.add(bottom);
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead)
	{
		Point2D.Double topLeft = anchor;
		Point2D.Double bottomRight = new Point2D.Double(anchor.x + getWidth(),
				anchor.y + getHeight() * 0.2);
		top.setBounds(topLeft, bottomRight);

		Point2D.Double topLeft1 = new Point2D.Double(anchor.x, anchor.y + getHeight() * 0.2);
		Point2D.Double bottomRight1 = new Point2D.Double(anchor.x + getWidth(), anchor.y
				+ getHeight() * 0.6);
		middle.setBounds(topLeft1, bottomRight1);
		text.setBounds(topLeft1, bottomRight1);

		Point2D.Double topLeft2 = new Point2D.Double(anchor.x, anchor.y + getHeight() * 0.6);
		Point2D.Double bottomRight2 = new Point2D.Double(anchor.x + getWidth(), anchor.y
				+ getHeight());
		bottom.setBounds(topLeft2, bottomRight2);
		
		this.invalidate();
	}
	
	@Override
	public ClassFigure clone()
	{
		ClassFigure other = (ClassFigure)super.clone();
		
		other.top = top.clone();
		other.middle = middle.clone();
		other.text = text.clone();
		other.bottom = bottom.clone();
		
		other.children = new ArrayList<Figure>();
		other.children.add(other.top);
		other.children.add(other.middle);
		other.children.add(other.text);
		other.children.add(other.bottom);
		
		return other;
	}
}
