//package husacct.graphics.task;
//
//import husacct.common.dto.ModuleDTO;
//import husacct.graphics.presentation.GraphicsGUI;
//import husacct.graphics.task.figures.IViolatedFigure;
//import husacct.graphics.task.figures.ModuleFigure;
//
//import javax.swing.JInternalFrame;
//
//public abstract class AbstractGUIController implements MouseClickListener
//{
//	// FIXME: Determine if the protected visibility is actually required or not.
//	// These were made protected to make sub-classing this controller for
//	// demonstrations purposes easy and to move the demonstration code
//	// outside of the actual library's code base.
//	protected GraphicsGUI graphicsGUI;
//	
//	public AbstractGUIController()
//	{
//		this.graphicsGUI = new JHotDrawGraphicsGUI();
//		this.graphicsGUI.addGUIListener(this);
//	}
//	
//	public JInternalFrame getGUI()
//	{
//		return this.graphicsGUI.getGUI();
//	}
//	
//	public void onModuleFigureZoom(ModuleFigure moduleFigure)
//	{
//		System.out.println("zoom on module "+moduleFigure.getModuleDTO().logicalPath);
//	}
//	
//	public void onViolatedFigureSelect(IViolatedFigure violatedFigure)
//	{
//		graphicsGUI.showViolations(violatedFigure.getViolations());
//	}
//	
//	protected void drawViolationsForShownModules()
//	{
//		/*
//		 * TODO
//		 * 
//		 * call
//		 */
//		this.graphicsGUI.getShownModuleFigures();
//		/*
//		 * get violations between all modules from ValidateServices
//		 */
//	}
//	
//	protected void drawModules(ModuleDTO[] moduleDTOs)
//	{
//		FigureFactory factory = new FigureFactory();
//		
//		for(ModuleDTO moduleDTO : moduleDTOs)
//		{
//			graphicsGUI.add(factory.createFigure(moduleDTO));
//		}
//	}
//}
