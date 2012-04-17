package husacct.graphics.presentation.figures;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Collection;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.decoration.ArrowTip;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.liner.Liner;
import org.jhotdraw.geom.BezierPath.Node;

public class RelationFigure extends BaseFigure implements ConnectionFigure
{
	private static final long serialVersionUID = 1805821357919823648L;
	private LineConnectionFigure line;

	public RelationFigure()
	{		
		super();
		
		this.line = new LineConnectionFigure();
		
		this.add(this.line);

		this.setLineStroke(new double[]{ 6.0 });

		ArrowTip arrowTip = new ArrowTip(0.5, 12, 3.0);
		this.set(AttributeKeys.END_DECORATION, arrowTip);
	}
	
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		line.setBounds(anchor, lead);
		
		super.setBounds(anchor, lead);
	}
	
	public void transform(AffineTransform tx) {
		
		line.transform(tx);
		line.updateConnection();
		
		super.transform(tx);
	}
	
	public void setLineColor(Color newColor) {
		set(AttributeKeys.STROKE_COLOR, newColor);
	}
	
	public void setLineThickness(double thickness) {
		set(AttributeKeys.STROKE_WIDTH, thickness);
	}
	
	public void setLineStroke(double[] stroke) {
		set(AttributeKeys.STROKE_DASHES, stroke);
	}
	
	@Override
	public Collection<Handle> createHandles(int detailLevel)
	{
		return this.line.createHandles(detailLevel);
	}
	
	@Override
	public RelationFigure clone() {
		RelationFigure other = (RelationFigure) super.clone();
		other.line = line.clone();
		
		return other; 
	}

	@Override
	public void setStartConnector(Connector start)
	{
		this.line.setStartConnector(start);
	}

	@Override
	public Connector getStartConnector()
	{
		return this.line.getStartConnector();
	}

	@Override
	public void setEndConnector(Connector end)
	{
		this.line.setEndConnector(end);
	}

	@Override
	public Connector getEndConnector()
	{
		return this.line.getEndConnector();
	}

	@Override
	public void updateConnection()
	{
		this.line.updateConnection();
	}

	@Override
	public boolean canConnect(Connector start, Connector end)
	{
		return this.line.canConnect(start, end);
	}

	@Override
	public boolean canConnect(Connector start)
	{
		return this.line.canConnect(start);
	}

	@Override
	public void setStartPoint(Double p)
	{
		this.line.setStartPoint(p);
	}

	@Override
	public void setEndPoint(Double p)
	{
		this.line.setEndPoint(p);
	}

	@Override
	public void setPoint(int index, Double p)
	{
		this.line.setPoint(index, p);
	}

	@Override
	public int getNodeCount()
	{
		return this.line.getNodeCount();
	}

	@Override
	public Double getPoint(int index)
	{
		return this.line.getPoint(index);
	}

	@Override
	public Node getNode(int index)
	{
		return this.line.getNode(index);
	}

	@Override
	public void setNode(int index, Node node)
	{
		this.line.setNode(index, node);
	}

	@Override
	public Figure getStartFigure()
	{
		return this.line.getStartFigure();
	}

	@Override
	public Figure getEndFigure()
	{
		return this.line.getEndFigure();
	}

	@Override
	public Liner getLiner()
	{
		return this.line.getLiner();
	}

	@Override
	public void setLiner(Liner newValue)
	{
		this.line.setLiner(newValue);
	}

	@Override
	public void lineout()
	{
		this.line.lineout();
	}
}
