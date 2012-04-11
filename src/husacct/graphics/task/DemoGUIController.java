package husacct.graphics.task;
import java.awt.geom.Rectangle2D;

import husacct.common.dto.ViolationDTO;
import husacct.graphics.task.figures.ModuleFigure;
import husacct.graphics.task.figures.ComponentFigure;
import husacct.graphics.task.figures.RelationFigure;
import husacct.graphics.task.figures.ViolatedComponentFigure;
import husacct.graphics.task.figures.ViolatedRelationFigure;


public class DemoGUIController extends GUIController {
	
	protected FigureConnectorStrategy connectionStrategy = new FigureConnectorStrategy();
	
	public DemoGUIController() {
		
		super();
		
		createMockupDrawing();
	}
	
	// FIXME: This code is just for demonstration purposes. Please remove it after demo'ing 
	// the prototype	
	private void createMockupDrawing()
	{		
		ModuleFigure leo = new ViolatedComponentFigure(new Rectangle2D.Double(10, 10, 150, 100), "Leo", new ViolationDTO[]{});
		drawing.add(leo);
		
		ModuleFigure christian = new ComponentFigure(new Rectangle2D.Double(10, 150, 150, 100), "Christian");
		drawing.add(christian);
		
		ModuleFigure michiel = new ComponentFigure(new Rectangle2D.Double(200, 75, 150, 100), "Michiel");
		drawing.add(michiel);
		
		RelationFigure relation1 = new RelationFigure();
		connectionStrategy.connect(relation1, leo, christian);
		drawing.add(relation1);
		
		RelationFigure relation2 = new RelationFigure();
		connectionStrategy.connect(relation2, christian, michiel);
		drawing.add(relation2);
		
		ViolatedRelationFigure relation3 = new ViolatedRelationFigure();
		connectionStrategy.connect(relation3, leo, michiel);
		drawing.add(relation3);
	}
}
