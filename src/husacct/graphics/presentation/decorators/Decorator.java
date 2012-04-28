package husacct.graphics.presentation.decorators;

import husacct.graphics.presentation.figures.BaseFigure;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D.Double;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Map;

import javax.swing.Action;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.DecoratedFigure;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.event.FigureListener;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.tool.Tool;
import org.jhotdraw.geom.Dimension2DDouble;

public abstract class Decorator extends BaseFigure implements DecoratedFigure {
	private static final long serialVersionUID = 1489931076171389065L;
	private BaseFigure decorator;

	public Decorator(BaseFigure decorator) {
		this.decorator = decorator;
	}

	public BaseFigure getDecorator() {
		return this.decorator;
	}

	@Override
	public void setDecorator(Figure decorator) {
		
		if (!(decorator instanceof BaseFigure)) 
			throw new RuntimeException("invalid decorator type");
	}

	@Override
	public void addFigureListener(FigureListener arg0) {
		decorator.addFigureListener(arg0);
	}

	@Override
	public void addNotify(Drawing arg0) {
		decorator.addNotify(arg0);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener arg0) {
		decorator.addPropertyChangeListener(arg0);
	}

	@Override
	public void changed() {
		decorator.changed();
	}

	@Override
	public boolean contains(Double arg0) {
		return decorator.contains(arg0);
	}

	@Override
	public Collection<Handle> createHandles(int arg0) {
		return decorator.createHandles(arg0);
	}

	@Override
	public void draw(Graphics2D arg0) {
		decorator.draw(arg0);
	}

	@Override
	public Connector findCompatibleConnector(Connector arg0, boolean arg1) {
		return decorator.findCompatibleConnector(arg0, arg1);
	}

	@Override
	public Connector findConnector(Double arg0, ConnectionFigure arg1) {
		return decorator.findConnector(arg0, arg1);
	}

	@Override
	public Figure findFigureInside(Double arg0) {
		return decorator.findFigureInside(arg0);
	}

	@Override
	public <T> T get(AttributeKey<T> arg0) {
		return decorator.get(arg0);
	}

	@Override
	public Collection<Action> getActions(Double arg0) {
		return decorator.getActions(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<AttributeKey, Object> getAttributes() {
		return decorator.getAttributes();
	}

	@Override
	public Object getAttributesRestoreData() {
		return decorator.getAttributesRestoreData();
	}

	@Override
	public java.awt.geom.Rectangle2D.Double getBounds() {
		return decorator.getBounds();
	}

	@Override
	public Collection<Connector> getConnectors(ConnectionFigure arg0) {
		return decorator.getConnectors(arg0);
	}

	@Override
	public Cursor getCursor(Double arg0) {
		return decorator.getCursor(arg0);
	}

	@Override
	public Collection<Figure> getDecomposition() {
		return decorator.getDecomposition();
	}

	@Override
	public java.awt.geom.Rectangle2D.Double getDrawingArea() {
		return decorator.getDrawingArea();
	}

	@Override
	public Double getEndPoint() {
		return decorator.getEndPoint();
	}

	@Override
	public int getLayer() {
		return decorator.getLayer();
	}

	@Override
	public Dimension2DDouble getPreferredSize() {
		return decorator.getPreferredSize();
	}

	@Override
	public Double getStartPoint() {
		return decorator.getStartPoint();
	}

	@Override
	public Tool getTool(Double arg0) {
		return decorator.getTool(arg0);
	}

	@Override
	public String getToolTipText(Double arg0) {
		return decorator.getToolTipText(arg0);
	}

	@Override
	public Object getTransformRestoreData() {
		return decorator.getTransformRestoreData();
	}

	@Override
	public boolean handleDrop(Double arg0, Collection<Figure> arg1, DrawingView arg2) {

		return decorator.handleDrop(arg0, arg1, arg2);
	}

	@Override
	public boolean handleMouseClick(Double arg0, MouseEvent arg1, DrawingView arg2) {

		return decorator.handleMouseClick(arg0, arg1, arg2);
	}

	@Override
	public boolean includes(Figure arg0) {
		return decorator.includes(arg0);
	}

	@Override
	public boolean isConnectable() {
		return decorator.isConnectable();
	}

	@Override
	public boolean isRemovable() {
		return decorator.isRemovable();
	}

	@Override
	public boolean isSelectable() {
		return decorator.isSelectable();
	}

	@Override
	public boolean isTransformable() {
		return decorator.isTransformable();
	}

	@Override
	public boolean isVisible() {
		return decorator.isVisible();
	}

	@Override
	public void remap(Map<Figure, Figure> arg0, boolean arg1) {

		decorator.remap(arg0, arg1);
	}

	@Override
	public void removeFigureListener(FigureListener arg0) {

		decorator.removeFigureListener(arg0);
	}

	@Override
	public void removeNotify(Drawing arg0) {

		decorator.removeNotify(arg0);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener arg0) {

		decorator.removePropertyChangeListener(arg0);
	}

	@Override
	public void requestRemove() {

		decorator.requestRemove();
	}

	@Override
	public void restoreAttributesTo(Object arg0) {
		decorator.restoreAttributesTo(arg0);
	}

	@Override
	public void restoreTransformTo(Object arg0) {
		decorator.restoreTransformTo(arg0);

	}

	@Override
	public <T> void set(AttributeKey<T> arg0, T arg1) {
		decorator.set(arg0, arg1);
	}

	@Override
	public void setBounds(Double arg0, Double arg1) {
		decorator.setBounds(arg0, arg1);
	}

	@Override
	public void transform(AffineTransform arg0) {
		decorator.transform(arg0);
	}

	@Override
	public void willChange() {
		decorator.willChange();
	}

	@Override
	public Decorator clone() {

		Decorator other = (Decorator) super.clone();
		other.decorator = decorator.clone();
		return other;
	}
}
