package husacct.graphics.presentation.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collection;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.decoration.ArrowTip;
import org.jhotdraw.draw.event.FigureEvent;
import org.jhotdraw.draw.event.FigureListener;
import org.jhotdraw.draw.handle.BezierNodeHandle;
import org.jhotdraw.draw.handle.BezierOutlineHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.liner.Liner;
import org.jhotdraw.geom.BezierPath.Node;

public class RelationFigure extends BaseFigure implements ConnectionFigure, FigureListener {
	private static final long serialVersionUID = 1805821357919823648L;
	private LineConnectionFigure line;
	private TextFigure amountFigure;

	public RelationFigure(String name, boolean violated, int amount) {
		super(name);

		line = new LineConnectionFigure();
		add(line);

		amountFigure = new TextFigure(Integer.toString(amount));
		add(amountFigure);

		line.addFigureListener(this);
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		line.updateConnection();
		relayout();
	}

	private void relayout() {
		double midX = line.getBounds().x + line.getBounds().width / 2;
		double midY = line.getBounds().y + line.getBounds().height / 2;

		amountFigure.willChange();
		amountFigure.setBounds(new Point2D.Double(midX, midY), null);
		amountFigure.changed();
	}

	public void transform(AffineTransform tx) {
		line.updateConnection();
		relayout();
	}

	public void setLineColor(Color newColor) {
		set(AttributeKeys.STROKE_COLOR, newColor);
		amountFigure.set(AttributeKeys.TEXT_COLOR, newColor);
	}

	public void setLineThickness(double thickness) {
		set(AttributeKeys.STROKE_WIDTH, thickness);
	}

	@Override
	public void draw(Graphics2D graphics) {
		ArrowTip arrowTip = new ArrowTip(0.5, 12, 3.0);
		set(AttributeKeys.END_DECORATION, arrowTip);

		double dashes = 4.0 / this.get(AttributeKeys.STROKE_WIDTH);
		set(AttributeKeys.STROKE_DASHES, new double[] { 6.0, dashes });

		super.draw(graphics);
	}

	@Override
	public Collection<Handle> createHandles(int detailLevel) {
		ArrayList<Handle> handles = new ArrayList<Handle>(getNodeCount());
		switch (detailLevel) {
		case -1:
			handles.add(new BezierOutlineHandle(line, true));
			break;
		case 0:
			handles.add(new BezierOutlineHandle(line));
			if (getLiner() == null) {
				for (int i = 1, n = getNodeCount() - 1; i < n; i++) {
					handles.add(new BezierNodeHandle(line, i));
				}
			}
			break;
		}
		return handles;
	}

	@Override
	public RelationFigure clone() {
		RelationFigure other = (RelationFigure) super.clone();
		other.children = new ArrayList<Figure>();
		other.line = line.clone();
		other.children.add(other.line);
		other.amountFigure = amountFigure.clone();
		other.children.add(other.amountFigure);

		return other;
	}

	@Override
	public void setStartConnector(Connector start) {
		line.setStartConnector(start);
	}

	@Override
	public Connector getStartConnector() {
		return line.getStartConnector();
	}

	@Override
	public void setEndConnector(Connector end) {
		line.setEndConnector(end);
	}

	@Override
	public Connector getEndConnector() {
		return line.getEndConnector();
	}

	@Override
	public void updateConnection() {
		line.updateConnection();
	}

	@Override
	public boolean canConnect(Connector start, Connector end) {
		return line.canConnect(start, end);
	}

	@Override
	public boolean canConnect(Connector start) {
		return line.canConnect(start);
	}

	@Override
	public void setStartPoint(Double p) {
		line.setStartPoint(p);
	}

	@Override
	public void setEndPoint(Double p) {
		line.setEndPoint(p);
	}

	@Override
	public void setPoint(int index, Double p) {
		line.setPoint(index, p);
	}

	@Override
	public int getNodeCount() {
		return line.getNodeCount();
	}

	@Override
	public Double getPoint(int index) {
		return line.getPoint(index);
	}

	@Override
	public Node getNode(int index) {
		return line.getNode(index);
	}

	@Override
	public void setNode(int index, Node node) {
		line.setNode(index, node);
	}

	@Override
	public Figure getStartFigure() {
		return line.getStartFigure();
	}

	@Override
	public Figure getEndFigure() {
		return line.getEndFigure();
	}

	@Override
	public Liner getLiner() {
		return line.getLiner();
	}

	@Override
	public void setLiner(Liner newValue) {
		this.willChange();
		line.setLiner(newValue);
		this.changed();
	}

	@Override
	public void lineout() {
		line.lineout();
	}

	@Override
	public boolean isModule() {
		return false;
	}

	@Override
	public boolean isLine() {
		return true;
	}

	public int getAmount() {
		return Integer.parseInt(amountFigure.getText());
	}
	
	@Override 
	public void willChange() {
		line.willChange();
		super.willChange();
	}
	
	@Override
	public void changed() {
		line.changed();
		super.changed();
	}	

	@Override
	public void areaInvalidated(FigureEvent e) {
		relayout();
	}

	@Override
	public void attributeChanged(FigureEvent e) {
	}

	@Override
	public void figureHandlesChanged(FigureEvent e) {
	}

	@Override
	public void figureChanged(FigureEvent e) {
	}

	@Override
	public void figureAdded(FigureEvent e) {
	}

	@Override
	public void figureRemoved(FigureEvent e) {
	}

	@Override
	public void figureRequestRemove(FigureEvent e) {
	}
}
