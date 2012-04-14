package husacct.graphics.task;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.jhotdraw.FigureConnectorStrategy;
import husacct.graphics.task.figures.ModuleFigure;


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
		FigureFactory figureFactory = new FigureFactory();
		
		ModuleDTO leoDTO = new ModuleDTO();
		leoDTO.logicalPath = "hu.leraren.Leo";
		leoDTO.type = "component";
		ViolationDTO leoError1 = new ViolationDTO(null, null, "weinig technische kennis", null, null, null, null);
		ViolationDTO leoError2 = new ViolationDTO(null, null, "praat veel", null, null, null, null);
		ModuleFigure leoFigure = (ModuleFigure)figureFactory.createFigure(leoDTO, new ViolationDTO[]{ leoError1, leoError2 }); 
		this.graphicsGUI.add(leoFigure);
		
		ModuleDTO christianDTO = new ModuleDTO();
		christianDTO.logicalPath = "hu.leraren.Christian";
		christianDTO.type = "component";
		ModuleFigure christianFigure = figureFactory.createFigure(christianDTO);
		this.graphicsGUI.add(christianFigure);
		
		ModuleDTO michielDTO = new ModuleDTO();
		michielDTO.logicalPath = "hu.leraren.Michiel";
		michielDTO.type = "component";
		ModuleFigure michielFigure = figureFactory.createFigure(michielDTO);
		this.graphicsGUI.add(michielFigure);
		
		
		DependencyDTO leoToChristian = new DependencyDTO(null, null, null, 0);
		this.graphicsGUI.addRelation(figureFactory.createFigure(leoToChristian), leoFigure, christianFigure);
	}
}
