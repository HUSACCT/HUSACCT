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
	protected int baseZIndex, zIndex, raiseZIndex;
	
	private ArrayList<Decorator> decorators = new ArrayList<Decorator>();
	private boolean isSizeable = false;
	private boolean isEnabled = true;
	private boolean isStoredInContainer = false;
	private String name;
	
	public BaseFigure(String theName) {
		super();
		name = theName;
		baseZIndex = 0;
		raiseZIndex = 5;
		zIndex = baseZIndex;
	}
	
	public void raiseLayer(){
		zIndex = raiseZIndex;
	}
	
	public void resetLayer(){
		zIndex = baseZIndex;
	}
	
	@Override
	public int getLayer(){
		return zIndex;
	}

	public String getName() {
		return name;
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
		// This function is used by the JHotDraw framework to draw the 'background' of a figure.
		// Since the BaseFigure is a composite figure it will not have to draw
		// it's background and therefore this function is empty. However, it cannot be removed
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
	
	public void updateLocation(double x, double y) {
		willChange();
		double widthX = x + getBounds().getWidth();
		double heightY = y + getBounds().getHeight();
		setBounds(new Point2D.Double(x, y), new Point2D.Double(widthX, heightY));
		changed();
	}

	@Override
	public Connector findConnector(Point2D.Double p, ConnectionFigure figure) {
		return new ChopRectangleConnector(this);
	}

	public boolean isParent(){
		return false;
	}
	
	public boolean isModule(){
		return false;
	}

	public boolean isLine(){
		return false;
	}
	
	public void setInContainer(boolean value) {
		this.isStoredInContainer = value;
	}
	
	public boolean isInContainer() {
		return isStoredInContainer;
	}
	
	public void setEnabled(boolean newValue) {
		isEnabled = newValue;
		setVisible(newValue);
		setSelectable(newValue);
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
}
