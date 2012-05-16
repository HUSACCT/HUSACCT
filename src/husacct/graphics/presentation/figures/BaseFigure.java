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
	private boolean isSizeable = false;

	private ArrayList<Decorator> decorators = new ArrayList<Decorator>();

	// private LinkedList<Connector> connectors = new LinkedList<Connector>();

	public BaseFigure() {
		super();
	}

	public void addDecorator(Decorator decorator) {
		decorators.add(decorator);
	}

	public void removeDecoratorByType(Class<?> searchClass) {
		ArrayList<Decorator> removes = new ArrayList<Decorator>();

		for (Decorator decorator : decorators) {
			if (decorator.getClass().isAssignableFrom(searchClass)) {
				removes.add(decorator);
			}
		}

		removeDecorators(removes.toArray(new Decorator[] {}));
	}

	public void removeDecorators(Decorator[] decorators) {
		for (Decorator decorator : decorators) {
			removeDecorator(decorator);
		}
	}

	public void removeDecorator(Decorator decorator) {
		willChange();
		decorator.deDecorate(this);
		decorators.remove(decorator);
		changed();
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

	@Override
	protected void drawFill(Graphics2D g) {
		// This function is used by the JHotDraw framework to draw the
		// 'background' of a figure
		// Since the BaseFigure is a composite figure it will not have to draw
		// it's background
		// and therefore this function is empty. However, it cannot be removed
		// because of the
		// requirements to override it.
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
	public BaseFigure clone() {
		BaseFigure other = (BaseFigure) super.clone();
		return other;
	}

	@Override
	public void draw(Graphics2D g) {
		for (Decorator decorator : this.decorators) {
			decorator.decorate(this);
		}

		set(AttributeKeys.CANVAS_FILL_COLOR, defaultBackgroundColor);

		super.draw(g);
	}

	@Override
	public Collection<Handle> createHandles(int detailLevel) {
		LinkedList<Handle> handles = new LinkedList<Handle>();
		if (isSizeable) {
			handles.addAll(createSizeableHandles(detailLevel));
		} else {
			handles.addAll(createSelectionHandles(detailLevel));
		}
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
		set(AttributeKeys.STROKE_COLOR, newColor);
	}

	public double getWidth() {
		return getBounds().width;
	}

	public double getHeight() {
		return getBounds().height;
	}

	public boolean isSizeable() {
		return isSizeable;
	}

	public void setSizeable(boolean newValue) {
		isSizeable = newValue;
	}

	@Override
	public Connector findConnector(Point2D.Double p, ConnectionFigure figure) {
		return new ChopRectangleConnector(this);
	}

	// TODO: Patrick: Re-enabled this code! Requires that the AbsoluteLocator
	// works so that
	// the location of connectors is properly determined.
	// @Override
	// public Collection<Connector> getConnectors(ConnectionFigure prototype) {
	// return (List<Connector>) Collections.unmodifiableList(connectors);
	// }
	//

	// LocatorConnector
	// // Point2D.Double bounds = this.getStartPoint();
	// //
	// // if (bounds.y < p.y) {
	// // // This figure is BELOW the other figure
	// // } else {
	// // // This figure is on the same level or below the other figure
	// // }
	// //
	// // LocatorConnector lc = new LocatorConnector(this,
	// RelativeLocator.north());
	// // connectors.add(lc);
	// //
	// // return lc;
	// }

	// @Override
	// public Connector findCompatibleConnector(Connector c, boolean isStart) {
	// if (c instanceof LocatorConnector) {
	// LocatorConnector lc = (LocatorConnector) c;
	// for (Connector cc : connectors) {
	// LocatorConnector lcc = (LocatorConnector) cc;
	// if (lcc.getLocator().equals(lc.getLocator())) {
	// return lcc;
	// }
	// }
	// }
	// return connectors.getFirst();
	// }

	public abstract boolean isModule();

	public abstract boolean isLine();
}
