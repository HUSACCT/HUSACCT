package husacct.graphics.task;

import java.awt.geom.Point2D;

import org.jhotdraw.draw.connector.Connector;

import husacct.graphics.task.figures.ModuleFigure;
import husacct.graphics.task.figures.RelationFigure;

public class FigureConnectorStrategy {
	
	private RelationFigure prototype;
	
	public FigureConnectorStrategy() {
		
		prototype = new RelationFigure();
	}
	
	public RelationFigure connect(ModuleFigure startFigure, ModuleFigure endFigure)
	{
		RelationFigure connection = new RelationFigure();
		
		return this.connect(connection, startFigure, endFigure);
	}
	
	public RelationFigure connect(RelationFigure fig, ModuleFigure startFigure, ModuleFigure endFigure) {
		
		Connector startConnector = startFigure.findConnector(new Point2D.Double(50, 50), prototype);
		Connector endConnector = endFigure.findConnector(new Point2D.Double(500, 30), prototype);
		
		if ((startConnector != null && endConnector != null) && prototype.canConnect(startConnector, endConnector))
		{			
			fig.willChange();
			fig.setStartConnector(startConnector);
			fig.setEndConnector(endConnector);
			fig.updateConnection();
			fig.changed();
			
			return fig;
		}
		
		throw new IllegalArgumentException("The figures cannot be connected"); 
	}
	
	// This code adds decorations to lines. Not needed at the moment
//	ArrowTip arrowTip = new ArrowTip(1.0, 12, 12, true, true, true);
//	connection.set(AttributeKeys.START_DECORATION, arrowTip);
//	arrowTip = new ArrowTip(0.5, 12, 3.0);
//	connection.set(AttributeKeys.END_DECORATION, arrowTip);
	
}
