package husacct.graphics.presentation.linelayoutstrategies;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.connector.ChopRectangleConnector;

public class DistantiatedChopRectangleConnector extends ChopRectangleConnector {
	private static final long serialVersionUID = 809986751164714212L;
	private double distance;

	public DistantiatedChopRectangleConnector(Figure owner, double distance) {
		super(owner);

		this.distance = distance;
	}

	@Override
	public Point2D.Double findStart(ConnectionFigure connection) {
		Figure startFigure = connection.getStartConnector().getOwner();
		Point2D.Double from;
		if (connection.getNodeCount() <= 2 || connection.getLiner() != null) {
			if (connection.getEndConnector() == null) {
				from = connection.getEndPoint();
			} else {
				Rectangle2D.Double r1 = getConnectorTarget(
						connection.getEndConnector().getOwner()).getBounds();
				from = distantiateFrom(connection, new Point2D.Double(r1.x
						+ r1.width / 2, r1.y + r1.height / 2));
			}
		} else {
			from = connection.getPoint(1);
		}
		return chop(startFigure, from);
	}

	@Override
	public Point2D.Double findEnd(ConnectionFigure connection) {
		Figure endFigure = connection.getEndConnector().getOwner();
		Point2D.Double from;
		if (connection.getNodeCount() <= 3
				&& connection.getStartFigure() == connection.getEndFigure()
				|| connection.getNodeCount() <= 2
				|| connection.getLiner() != null) {
			if (connection.getStartConnector() == null) {
				from = connection.getStartPoint();
			} else if (connection.getStartFigure() == connection.getEndFigure()) {
				Rectangle2D.Double r1 = getConnectorTarget(
						connection.getStartConnector().getOwner()).getBounds();
				from = distantiateFrom(connection, new Point2D.Double(r1.x
						+ r1.width / 2, r1.y));
			} else {
				Rectangle2D.Double r1 = getConnectorTarget(
						connection.getStartConnector().getOwner()).getBounds();
				from = distantiateFrom(connection, new Point2D.Double(r1.x
						+ r1.width / 2, r1.y + r1.height / 2));
			}
		} else {
			from = connection.getPoint(connection.getNodeCount() - 2);
		}

		return chop(endFigure, from);
	}

	private Point2D.Double distantiateFrom(ConnectionFigure connection,
			Point2D.Double from) {
		Point2D.Double pointStart = connection.getStartPoint();
		Point2D.Double pointEnd = connection.getEndPoint();

		Point2D.Double movement = husacct.graphics.util.Geom
				.getPointMovementFromLineAngle(pointStart, pointEnd, distance);

		from.x += movement.x;
		from.y += movement.y;

		return from;
	}

}
