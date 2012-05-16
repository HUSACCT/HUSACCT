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
	private double distance;
	
	public RoundedLiner(double distance) {
		this.distance = distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public void lineout(ConnectionFigure figure) {
        BezierPath path = ((LineConnectionFigure) figure).getBezierPath();
        Connector start = figure.getStartConnector();
        Connector end = figure.getEndConnector();
        if (start == null || end == null || path == null) {
            return;
        }
        
        Point2D.Double centerPoint = path.getCenter();
        
        Node startNode = path.get(0);
        Node endNode = path.get(path.size()-1);
        path.clear();
        
        path.add(startNode);
        
        
        Point2D.Double pointStart = new Point2D.Double(startNode.x[0], startNode.y[0]);
        Point2D.Double pointEnd = new Point2D.Double(endNode.x[0], endNode.y[0]);
        
        double width;
        if(pointStart.x > pointEnd.x)
        {
        	width = pointStart.x - pointEnd.x;
        }
        else
        {
        	width = pointEnd.x - pointStart.x;
        }

        double height;
        if(pointStart.y > pointEnd.y)
        {
        	height = pointStart.y - pointEnd.y;
        }
        else
        {
        	height = pointEnd.y - pointStart.y; 
        }
        
        double diffX = width/(width+height);
        double diffY = height/(width+height);
               
        centerPoint.x += diffX*this.distance;
        centerPoint.y += diffY*this.distance;
        Node centerNode = new Node(centerPoint);
        path.add(centerNode);
        
        path.add(endNode);
        

        path.invalidatePath();
	}

	@Override
	public Collection<Handle> createHandles(BezierPath path) {
		return Collections.emptyList();
	}
	
	public Liner clone() {
        try {
            return (Liner) super.clone();
        } catch (CloneNotSupportedException ex) {
            InternalError error = new InternalError(ex.getMessage());
            error.initCause(ex);
            throw error;
        }
	}

}
