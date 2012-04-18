package husacct.graphics.task;

import husacct.graphics.presentation.figures.BaseFigure;

import java.awt.geom.Point2D;

import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.connector.Connector;

public class FigureConnectorStrategy {
	
	private ConnectionFigure prototype;
	
	public FigureConnectorStrategy() {
		
		prototype = new LineConnectionFigure();
	}
	
	public Figure connect(BaseFigure startFigure, BaseFigure endFigure) {	
		Connector startConnector = startFigure.findConnector(new Point2D.Double(50, 50), prototype);
		Connector endConnector = endFigure.findConnector(new Point2D.Double(500, 30), prototype);
		
		if ((startConnector != null && endConnector != null) && prototype.canConnect(startConnector, endConnector))
		{			
			ConnectionFigure connection = (ConnectionFigure)prototype.clone();
			connection.willChange();
			connection.setStartConnector(startConnector);
			connection.setEndConnector(endConnector);
			connection.updateConnection();
			connection.changed();
			
			TextFigure tf = new TextFigure("Hoi!");
			tf.setDecorator(connection);
			return tf;
		}
		
		throw new IllegalArgumentException("The figures cannot be connected"); 
	}
	
	public ConnectionFigure getConnectionPrototype() {
		return prototype;
	}
	
	public void setConnectionPrototype(ConnectionFigure newPrototype) {
		prototype = newPrototype;
	}
	
	
	
	// This code adds decorations to lines. Not needed at the moment
//	ArrowTip arrowTip = new ArrowTip(1.0, 12, 12, true, true, true);
//	connection.set(AttributeKeys.START_DECORATION, arrowTip);
//	arrowTip = new ArrowTip(0.5, 12, 3.0);
//	connection.set(AttributeKeys.END_DECORATION, arrowTip);
	
}
