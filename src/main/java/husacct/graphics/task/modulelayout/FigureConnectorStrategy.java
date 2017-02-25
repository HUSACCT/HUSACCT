package husacct.graphics.task.modulelayout;

import husacct.graphics.domain.figures.BaseFigure;

import java.awt.geom.Point2D;

import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.connector.Connector;

public class FigureConnectorStrategy {
	
	private ConnectionFigure	prototype;
	
	public FigureConnectorStrategy() {
		prototype = new LineConnectionFigure();
	}
	
	public Figure connect(BaseFigure startFigure, BaseFigure endFigure) {
		ConnectionFigure connection = (ConnectionFigure) prototype.clone();
		return connect(connection, startFigure, endFigure);
	}
	
	public ConnectionFigure connect(ConnectionFigure connection, BaseFigure startFigure, BaseFigure endFigure) {
		Connector startConnector = startFigure.findConnector(new Point2D.Double(50, 50), prototype);
		Connector endConnector = endFigure.findConnector(new Point2D.Double(500, 30), prototype);
		
		if (startConnector != null && endConnector != null && prototype.canConnect(startConnector, endConnector)) {
			connection.willChange();
			connection.setStartConnector(startConnector);
			connection.setEndConnector(endConnector);
			connection.updateConnection();
			connection.changed();
			
			return connection;
		}
		
		throw new IllegalArgumentException("The figures cannot be connected");
	}
	
	public ConnectionFigure getConnectionPrototype() {
		return prototype;
	}
	
	public void setConnectionPrototype(ConnectionFigure newPrototype) {
		prototype = newPrototype;
	}
}
