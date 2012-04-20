package husacct.graphics.presentation.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.LinkedList;

import org.jhotdraw.draw.AbstractAttributedCompositeFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.TransformHandleKit;

public abstract class BaseFigure extends AbstractAttributedCompositeFigure {
	private static final long serialVersionUID = 971276235252293165L;
	protected Point2D.Double anchor;
	protected Point2D.Double lead;
	protected String name;

	public BaseFigure() {
		this(new Rectangle2D.Double(0, 0, 0, 0), "");
	}
	
	public BaseFigure(Rectangle2D.Double rect, String name) {
		super();

		this.anchor = new Point2D.Double(rect.x, rect.y);
		this.lead = new Point2D.Double(rect.x + rect.width, rect.y
				+ rect.height);
		this.name = name;

		initializeComponents();
	}

	protected void initializeComponents() {
		setBounds(anchor, lead);
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {

		this.anchor = anchor;
		this.lead = lead;
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


	public double getWidth() {
		return lead.x - anchor.x;
	}

	public double getHeight() {
		return lead.y - anchor.y;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;

		invalidate();
	}
}
