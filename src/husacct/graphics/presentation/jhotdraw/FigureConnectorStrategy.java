package husacct.graphics.presentation.jhotdraw;

import java.awt.geom.Point2D;

import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.connector.Connector;

import husacct.graphics.presentation.jhotdraw.figures.JHotDrawModuleFigure;
import husacct.graphics.presentation.jhotdraw.figures.JHotDrawRelationFigure;

public class FigureConnectorStrategy
{	
	public FigureConnectorStrategy() {
	}
	
	public void connect(JHotDrawRelationFigure fig, JHotDrawModuleFigure startFigure, JHotDrawModuleFigure endFigure)
	{
		LineConnectionFigure prototype = new LineConnectionFigure();
		
		Connector startConnector = startFigure.findConnector(new Point2D.Double(50, 50), prototype);
		Connector endConnector = endFigure.findConnector(new Point2D.Double(500, 30), prototype);
		
		if ((startConnector != null && endConnector != null) && prototype.canConnect(startConnector, endConnector))
		{			
			fig.willChange();
			fig.setStartConnector(startConnector);
			fig.setEndConnector(endConnector);
			fig.updateConnection();
			fig.changed();
		}
		else
		{
			throw new IllegalArgumentException("The figures cannot be connected");
		}
	}
	
	// This code adds decorations to lines. Not needed at the moment
//	ArrowTip arrowTip = new ArrowTip(1.0, 12, 12, true, true, true);
//	connection.set(AttributeKeys.START_DECORATION, arrowTip);
//	arrowTip = new ArrowTip(0.5, 12, 3.0);
//	connection.set(AttributeKeys.END_DECORATION, arrowTip);
	
}
