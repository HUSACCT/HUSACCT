package husacct.graphics.task;

import java.awt.Dimension;

import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.GraphicsFrame;
//import husacct.graphics.task.figures.ModuleFigure;
//import husacct.graphics.task.figures.FigureFactory;

import javax.swing.JInternalFrame;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.event.FigureSelectionEvent;
import org.jhotdraw.draw.event.FigureSelectionListener;

public abstract class GUIController implements IDrawingListener
{
	// FIXME: Determine if the protected visibility is actually required or not.
	// These were made protected to make sub-classing this controller for
	// demonstrations purposes easy and to move the demonstration code
	// outside of the actual library's code base.
	protected Drawing drawing;
	protected DrawingView view;
	protected GraphicsFrame drawTarget;
	
	public GUIController() {
		
		initializeComponents();
	}
	
	private void initializeComponents() {
		drawing = new Drawing();
		drawing.addDrawingListener(this);
		view = new DrawingView(drawing);
		view.setPreferredSize(new Dimension(500,500));
		
		drawTarget = new GraphicsFrame(view);
	}
	
	public JInternalFrame getGUI() {
		return drawTarget;
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

	@Override
	public void onModuleZoom(ModuleDTO moduleDTO)
	{
		// TODO clear drawing, get child modules, draw child modules
	}
	
	@Override
	public void onViolationsSelected(ViolationDTO[] violationDTOs)
	{
	}
	
//	private void drawFiguresToDrawing(List<ModuleFigure> figures,Drawing drawing){
//		for(ModuleFigure figure : figures){
//			drawing.add(figure);
//		}
//	}
	
}
