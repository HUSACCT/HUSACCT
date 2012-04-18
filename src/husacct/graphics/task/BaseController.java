package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.GraphicsFrame;
import husacct.graphics.presentation.figures.FigureFactory;

import java.awt.Dimension;

import javax.swing.JInternalFrame;

public abstract class BaseController implements MouseClickListener
{
	protected Drawing drawing;
	protected DrawingView view;
	protected GraphicsFrame drawTarget;

	protected FigureFactory figureFactory;
	protected FigureConnectorStrategy connectionStrategy;

	public BaseController() {

		figureFactory = new FigureFactory();
		connectionStrategy = new FigureConnectorStrategy();

		initializeComponents();
	}
	
	private void initializeComponents() {
		drawing = new Drawing();
		view = new DrawingView(drawing);
		view.setPreferredSize(new Dimension(500, 500));
		view.addListener(this);

		drawTarget = new GraphicsFrame(view);
	}

	public JInternalFrame getGUI() {
		return drawTarget;
	}

	@Override
	public void showViolations(ViolationDTO[] violationDTOs)
	{
		System.out.println("Show "+violationDTOs.length+" violations");
	}
	
	public abstract void drawArchitecture(DrawingDetail detail);
	
	public void drawViolationsForShownModules()
	{
	}
}
