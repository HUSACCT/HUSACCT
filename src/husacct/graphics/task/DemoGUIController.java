package husacct.graphics.task;
import java.awt.geom.Rectangle2D;

import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.jhotdraw.FigureConnectorStrategy;
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
		ModuleDTO leoDTO = new ModuleDTO();
		leoDTO.logicalPath = "leraren.Leo";
		ViolationDTO[] violations = new ViolationDTO[]
		{
			new ViolationDTO("leraren.Leo", "leraren.Michiel", "problemen", null, null, null, null)
		};
		ModuleFigure leo = new ViolatedComponentFigure(new Rectangle2D.Double(10, 10, 150, 100), leoDTO, violations);
		graphicsGUI.add(leo);
		
		ModuleDTO christianDTO = new ModuleDTO();
		christianDTO.logicalPath = "leraren.Christian";
		ModuleFigure christian = new ComponentFigure(new Rectangle2D.Double(10, 150, 150, 100), christianDTO);
		graphicsGUI.add(christian);
		
		ModuleDTO michielDTO = new ModuleDTO();
		michielDTO.logicalPath = "leraren.Michiel";
		ModuleFigure michiel = new ComponentFigure(new Rectangle2D.Double(200, 75, 150, 100), michielDTO);
		graphicsGUI.add(michiel);
		
		RelationFigure relation1 = new RelationFigure();
		graphicsGUI.addRelation(relation1, leo, christian);
		
		RelationFigure relation2 = new RelationFigure();
		graphicsGUI.addRelation(relation2, christian, michiel);
		
		ViolatedRelationFigure relation3 = new ViolatedRelationFigure();
		graphicsGUI.addRelation(relation3, michiel, leo);
	}
}
