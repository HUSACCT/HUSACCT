package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jhotdraw.draw.Figure;

public class AbsoluteLocator implements org.jhotdraw.draw.locator.Locator {

	private Point2D.Double getFigureCenter(Figure figure) {
		Rectangle2D.Double bounds = figure.getBounds();

		return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
	}

	@Override
	public Point2D.Double locate(Figure owner) {

		return (Point2D.Double) owner.getStartPoint().clone();
	}

	@Override
	public Point2D.Double locate(Figure owner, Figure dependent) {
		Point2D.Double ownerCenter = getFigureCenter(owner);
		Point2D.Double dependentCenter = getFigureCenter(dependent);
		Rectangle2D.Double ownerBounds = owner.getBounds();

		double connectorX = 0.0, connectorY = 0.0;

		if (ownerCenter.y < dependentCenter.y)
			// We should connect to the NORTH side of owner
			connectorY = ownerBounds.y;
		else if (ownerCenter.y > dependentCenter.y)
			// We should connect to the SOUTH side of owner
			connectorY = ownerBounds.y + ownerBounds.height;
		else
			// Use ownerCenter.y as location for the connector
			connectorY = ownerCenter.y;

		if (ownerCenter.x < dependentCenter.x)
			// We should connect to the WEST side of owner
			connectorX = ownerBounds.x + ownerBounds.width;
		else if (ownerCenter.x > dependentCenter.x)
			// We should connect to the EAST side of owner
			connectorX = ownerBounds.x;
		else
			// We should use ownerCenter.x as x.
			connectorX = ownerCenter.x;

		return new Point2D.Double(connectorX, connectorY);
	}

}
