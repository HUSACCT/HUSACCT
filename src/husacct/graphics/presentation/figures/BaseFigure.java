package husacct.graphics.presentation.figures;

import husacct.graphics.presentation.decorators.Decorator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.jhotdraw.draw.AbstractAttributedCompositeFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.connector.ChopRectangleConnector;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.Handle;

public abstract class BaseFigure extends AbstractAttributedCompositeFigure {
	private static final long serialVersionUID = 971276235252293165L;

	public static final Color defaultBackgroundColor = new Color(252, 255, 182);
	public static final Color projectBackground = new Color(156, 244, 116);
	protected int baseZIndex, zIndex, raiseZIndex;

	private final ArrayList<Decorator> decorators = new ArrayList<Decorator>();
	private boolean isSizeable = false;
	private boolean isEnabled = true;
	private boolean isStoredInContainer = false;
	private final String name;
	private boolean isContext = false;

	public BaseFigure(String theName) {
		super();
		this.name = theName;
		this.baseZIndex = 0;
		this.raiseZIndex = 5;
		this.zIndex = this.baseZIndex;
	}

	public void addDecorator(Decorator decorator) {
		this.decorators.add(decorator);
	}

	@Override
	public BaseFigure clone() {
		BaseFigure other = (BaseFigure) super.clone();
		return other;
	}

	@Override
	public Collection<Handle> createHandles(int detailLevel) {
		LinkedList<Handle> handles = new LinkedList<Handle>();
		if (this.isSizeable) {
			handles.addAll(this.createSizeableHandles(detailLevel));
		} else {
			handles.addAll(this.createSelectionHandles(detailLevel));
		}
		return handles;
	}

	private Collection<Handle> createSelectionHandles(int detailLevel) {
		LinkedList<Handle> handles = new LinkedList<Handle>();
		if (detailLevel == 0) {
			Handle handle = new BoundsOutlineHandle(this, false, false);
			handles.add(handle);
		}
		return handles;
	}

	private Collection<Handle> createSizeableHandles(int detailLevel) {
		return super.createHandles(detailLevel);
	}

	@Override
	public void draw(Graphics2D g) {
		for (Decorator decorator : this.decorators) {
			decorator.decorate(this);
		}

		this.set(AttributeKeys.CANVAS_FILL_COLOR, defaultBackgroundColor);

		super.draw(g);
	}

	@Override
	protected void drawFill(Graphics2D g) {
		// This function is used by the JHotDraw framework to draw the
		// 'background' of a figure.
		// Since the BaseFigure is a composite figure it will not have to draw
		// it's background and therefore this function is empty. However, it
		// cannot be removed
		// because of the requirements to override it.
	}

	@Override
	protected void drawStroke(Graphics2D g) {
		// This function is used by the JHotDraw framework to draw the outline
		// of a figure
		// Since the BaseFigure is a composite figure it will not have to draw
		// it's outline
		// and therefore this function is empty. However, it cannot be removed
		// because of the
		// requirements to override it.
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}

		BaseFigure other = (BaseFigure) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public Connector findConnector(Point2D.Double p, ConnectionFigure figure) {
		return new ChopRectangleConnector(this);
	}

	public double getHeight() {
		return this.getBounds().height;
	}

	@Override
	public int getLayer() {
		return this.zIndex;
	}

	public String getName() {
		return this.name;
	}

	public double getWidth() {
		return this.getBounds().width;
	}

	public boolean isContext() {
		return this.isContext;
	}

	public void isContext(boolean b) {
		this.isContext = b;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public boolean isInContainer() {
		return this.isStoredInContainer;
	}

	public boolean isLine() {
		return false;
	}

	public boolean isModule() {
		return false;
	}

	public boolean isParent() {
		return false;
	}

	public boolean isSizeable() {
		return this.isSizeable;
	}

	public void raiseLayer() {
		this.zIndex = this.raiseZIndex;
	}

	public void removeDecorator(Decorator decorator) {
		this.willChange();
		decorator.deDecorate(this);
		this.decorators.remove(decorator);
		this.changed();
	}

	public void removeDecoratorByType(Class<?> searchClass) {
		ArrayList<Decorator> removes = new ArrayList<Decorator>();

		for (Decorator decorator : this.decorators) {
			if (decorator.getClass().isAssignableFrom(searchClass)) {
				removes.add(decorator);
			}
		}

		this.removeDecorators(removes.toArray(new Decorator[] {}));
	}

	public void removeDecorators(Decorator[] decorators) {
		for (Decorator decorator : decorators) {
			this.removeDecorator(decorator);
		}
	}

	public void resetLayer() {
		this.zIndex = this.baseZIndex;
	}

	public void setEnabled(boolean newValue) {
		this.isEnabled = newValue;
		this.setVisible(newValue);
		this.setSelectable(newValue);
	}

	public void setInContainer(boolean value) {
		this.isStoredInContainer = value;
	}

	public void setSizeable(boolean newValue) {
		this.isSizeable = newValue;
	}

	public void setStrokeColor(Color newColor) {
		this.set(AttributeKeys.STROKE_COLOR, newColor);
	}

	@Override
	public void transform(AffineTransform at) {
		Point2D.Double anchor = this.getStartPoint();
		Point2D.Double lead = this.getEndPoint();

		Point2D.Double newAnchor = new Point2D.Double(0, 0), newLead = new Point2D.Double(
				0, 0);
		newAnchor = (Point2D.Double) at.transform(anchor, newAnchor);
		newLead = (Point2D.Double) at.transform(lead, newLead);

		this.setBounds(newAnchor, newLead);
	}

	public void updateLocation(double x, double y) {
		this.willChange();
		double widthX = x + this.getBounds().getWidth();
		double heightY = y + this.getBounds().getHeight();
		this.setBounds(new Point2D.Double(x, y), new Point2D.Double(widthX,
				heightY));
		this.changed();
	}
}
