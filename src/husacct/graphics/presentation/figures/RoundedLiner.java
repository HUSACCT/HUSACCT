package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;

import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.liner.Liner;
import org.jhotdraw.geom.BezierPath;
import org.jhotdraw.geom.BezierPath.Node;

public class RoundedLiner implements Liner {
	public static double getAngle(Point2D.Double point1, Point2D.Double point2) {
		double dx = point1.x - point2.x;
		// Minus to correct for coord re-mapping
		double dy = -(point1.y - point2.y);
		
		double inRads = Math.atan2(dy, dx);
		
		// We need to map to coord system when 0 degree is at 3 O'clock, 270 at
		// 12 O'clock
		if (inRads < 0) inRads = Math.abs(inRads);
		else
			inRads = 2 * Math.PI - inRads;
		
		return Math.toDegrees(inRads);
	}
	
	private double	distance;
	
	public RoundedLiner(double distance) {
		this.distance = distance;
	}
	
	@Override
	public Liner clone() {
		try {
			return (Liner) super.clone();
		} catch (CloneNotSupportedException ex) {
			InternalError error = new InternalError(ex.getMessage());
			error.initCause(ex);
			throw error;
		}
	}
	
	@Override
	public Collection<Handle> createHandles(BezierPath path) {
		return Collections.emptyList();
	}
	
	@Override
	public void lineout(ConnectionFigure figure) {
		BezierPath path = ((LineConnectionFigure) figure).getBezierPath();
		Connector start = figure.getStartConnector();
		Connector end = figure.getEndConnector();
		if (start == null || end == null || path == null) return;
		
		Point2D.Double centerPoint = path.getCenter();
		
		Node startNode = path.get(0);
		Node endNode = path.get(path.size() - 1);
		path.clear();
		
		path.add(startNode);
		
		Point2D.Double pointStart = new Point2D.Double(startNode.x[0],
				startNode.y[0]);
		Point2D.Double pointEnd = new Point2D.Double(endNode.x[0], endNode.y[0]);
		
		double width;
		if (pointStart.x > pointEnd.x) width = pointStart.x - pointEnd.x;
		else
			width = pointEnd.x - pointStart.x;
		
		double height;
		if (pointStart.y > pointEnd.y) height = pointStart.y - pointEnd.y;
		else
			height = pointEnd.y - pointStart.y;
		
		double diffX = width / (width + height);
		double diffY = height / (width + height);
		
		double angle = RoundedLiner.getAngle(pointStart, pointEnd);
		if (angle < 90 || angle > 180 && angle < 270) {
			centerPoint.x += diffY * distance;
			centerPoint.y -= diffX * distance;
		} else {
			centerPoint.x += diffY * distance;
			centerPoint.y += diffX * distance;
		}
		
		Node centerNode = new Node(centerPoint);
		path.add(centerNode);
		
		path.add(endNode);
		
		path.invalidatePath();
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
}
