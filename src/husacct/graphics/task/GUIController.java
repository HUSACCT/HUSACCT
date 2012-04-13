package husacct.graphics.task;

import husacct.graphics.presentation.GraphicsGUI;
import husacct.graphics.task.figures.IViolatedFigure;
import husacct.graphics.task.figures.ModuleFigure;

import javax.swing.JInternalFrame;

public abstract class GUIController implements IGraphicsGUIListener
{
	// FIXME: Determine if the protected visibility is actually required or not.
	// These were made protected to make sub-classing this controller for
	// demonstrations purposes easy and to move the demonstration code
	// outside of the actual library's code base.
	protected GraphicsGUI graphicsGUI;
	
	public GUIController()
	{
		this.graphicsGUI = new husacct.graphics.presentation.jhotdraw.GraphicsGUI();
		this.graphicsGUI.addGUIListener(this);
	}
	
	public JInternalFrame getGUI()
	{
		return this.graphicsGUI.getGUI();
	}
	
	public void onModuleFigureZoom(ModuleFigure moduleFigure)
	{
		System.out.println("zoom on module "+moduleFigure.getModuleDTO().logicalPath);
	}
	
	public void onViolatedFigureSelect(IViolatedFigure violatedFigure)
	{
		graphicsGUI.showViolations(violatedFigure.getViolations());
	}
		
	public void drawAnalysedArchitecture(DrawingDetail detail) {
//		DefineService analyseservice = new AnalysedService();
		// TODO: This should be the method we are going to use.
		// ServiceProvider serviceprovider = new ServiceProvider();
		// defineservice = serviceprovider.getDefineService();
//		ModuleDTO[] modules = analyseservice.getDefinedLayers();
//		this.drawModules(modules);
		
		
		if(detail==DrawingDetail.WITH_VIOLATIONS){
			drawViolationsForShownModules();
		}
	}
	
	public void drawDefinedArchitecture(DrawingDetail detail) { 
//		DefineService defineservice = new DefineService();
		// TODO: This should be the method we are going to use.
		// ServiceProvider serviceprovider = new ServiceProvider();
		// defineservice = serviceprovider.getDefineService();
//		ModuleDTO[] layers = defineservice.getDefinedLayers();
//		this.drawModules(layers);
		
		if(detail==DrawingDetail.WITH_VIOLATIONS){
			drawViolationsForShownModules();
		}
	}
	
	private void drawViolationsForShownModules(){
		//Validate service
	}
	
//	private void drawModules(ModuleDTO[] moduleDTOs) {
//		for (ModuleDTO dto : moduleDTOs) {
//			ModuleFigure f = (ModuleFigure)FigureFactory.getFigure(dto);
//			
//			// FIXME: Determine the proper position of each created figure as they're
//			// all located at (10, 10) with a size of (100, 75). Moving BaseModuleFigure
//			// figures can be done with AffineTransform.translate or by calling setBounds.
//			// Using an AffineTransform is preferred.
//			
//			drawing.add(f);
//		}
////		List<ModuleFigure> figures = new ArrayList<ModuleFigure>();
////		ModuleFigureFactory factory = new ModuleFigureFactory();
////		for(ModuleDTO moduleDTO : modulesDTOs){
////			ModuleFigure figure = factory.createFigure(moduleDTO);
////			figures.add(figure);
////		}
////		
////		drawFiguresToDrawing(figures,drawing);
//	}
	
//	private void drawFiguresToDrawing(List<ModuleFigure> figures,Drawing drawing){
//		for(ModuleFigure figure : figures){
//			drawing.add(figure);
//		}
//	}
	
}
