package husacct.graphics.presentation.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;

import org.jhotdraw.draw.AbstractAttributedCompositeFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.Handle;

public abstract class BaseFigure extends AbstractAttributedCompositeFigure {
	private static final long serialVersionUID = 971276235252293165L;
	private boolean isSizeable = false;
	private boolean violated = false;

	public BaseFigure(boolean violated) {
		super();

		this.violated = violated;
	}

	@Override
	public void transform(AffineTransform at) {
		Point2D.Double anchor = getStartPoint();
		Point2D.Double lead = getEndPoint();

		Point2D.Double newAnchor = new Point2D.Double(0, 0), newLead = new Point2D.Double(0, 0);
		newAnchor = (Point2D.Double) at.transform(anchor, newAnchor);
		newLead = (Point2D.Double) at.transform(lead, newLead);

		setBounds(newAnchor, newLead);
	}

	// TODO: This should be a decorator!
	public void setViolated(boolean violated) {
		this.willChange();
		this.violated = violated;
		this.changed();
	}

	public boolean isViolated() {
		return this.violated;
	}

	@Override
	protected void drawFill(Graphics2D g) {
		// Empty
		// TODO: Sorry? What do you do?
	}

	@Override
	protected void drawStroke(Graphics2D g) {
		// Empty
		// TODO: Sorry? What do you do?
	}

	@Override
	public BaseFigure clone() {
		BaseFigure other = (BaseFigure) super.clone();

		return other;
	}

	@Override
	public Collection<Handle> createHandles(int detailLevel) {
		LinkedList<Handle> handles = new LinkedList<Handle>();
		if (isSizeable)
			handles.addAll(createSizeableHandles(detailLevel));
		else
			handles.addAll(createSelectionHandles(detailLevel));

		return handles;
	}

	private Collection<Handle> createSizeableHandles(int detailLevel) {
		return super.createHandles(detailLevel);
	}

	private Collection<Handle> createSelectionHandles(int detailLevel) {
		LinkedList<Handle> handles = new LinkedList<Handle>();

		if (detailLevel == 0) {
			Handle handle = new BoundsOutlineHandle(this, false, false);
			handles.add(handle);
		}

		return handles;
	}

	public void setStrokeColor(Color newColor) {
		this.set(AttributeKeys.STROKE_COLOR, newColor);
	}

	public double getWidth() {
		return this.getBounds().width;
	}

	public double getHeight() {
		return this.getBounds().height;
	}

	public boolean isSizeable() {

		return this.isSizeable;
	}

	public void setSizeable(boolean newValue) {

		this.isSizeable = newValue;
	}

	public abstract boolean isModule();

	public abstract boolean isLine();
}
