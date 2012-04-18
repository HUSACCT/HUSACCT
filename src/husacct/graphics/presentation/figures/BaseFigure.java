package husacct.graphics.presentation.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;

import org.jhotdraw.draw.AbstractAttributedCompositeFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.DecoratedFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.TransformHandleKit;

public abstract class BaseFigure extends AbstractAttributedCompositeFigure implements DecoratedFigure {
	private static final long serialVersionUID = 971276235252293165L;
	
	private Figure decorator = null;

	public BaseFigure()
	{
		super();
	}

	@Override
	public void transform(AffineTransform at) {
		Point2D.Double anchor = getStartPoint();
		Point2D.Double lead = getEndPoint();

		Point2D.Double newAnchor = new Point2D.Double(0, 0), newLead = new Point2D.Double(
				0, 0);
		newAnchor = (Point2D.Double) at.transform(anchor, newAnchor);
		newLead = (Point2D.Double) at.transform(lead, newLead);

		setBounds(newAnchor, newLead);
	}

	@Override
	protected void drawFill(Graphics2D g) {
		// Empty
	}

	@Override
	protected void drawStroke(Graphics2D g) {
		// Empty
	}

	@Override
	public BaseFigure clone() {
		BaseFigure other = (BaseFigure) super.clone();

		return other;
	}

	@Override
	public Collection<Handle> createHandles(int detailLevel) {
		LinkedList<Handle> handles = new LinkedList<Handle>();
		if (detailLevel == 0) {
			TransformHandleKit.addScaleMoveTransformHandles(this, handles);
		}

		return handles;
	}
	
	public void setStrokeColor(Color newColor) {
		this.set(AttributeKeys.STROKE_COLOR, newColor);
	}

	public double getWidth()
	{
		return this.getBounds().width;
	}

	public double getHeight()
	{
		return this.getBounds().height;
	}
	
	public void setDecorator(Figure newDecorator) {
		decorator = newDecorator;
	}
  
	public Figure getDecorator() {
		return decorator;
	}	
}
