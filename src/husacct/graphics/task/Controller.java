package husacct.graphics.task;

import husacct.analyse.AnalyseServiceStub;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.GraphicsFrame;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.FigureFactory;

import java.awt.Dimension;

import javax.swing.JInternalFrame;

public class Controller implements MouseClickListener
{
	// FIXME: Determine if the protected visibility is actually required or not.
	// These were made protected to make sub-classing this controller for
	// demonstrations purposes easy and to move the demonstration code
	// outside of the actual library's code base.
	protected Drawing drawing;
	protected DrawingView view;
	protected GraphicsFrame drawTarget;

	private IAnalyseService analyseService;
	private FigureFactory figureFactory;
	private FigureConnectorStrategy connectionStrategy;

	public Controller() {

		figureFactory = new FigureFactory();
		analyseService = new AnalyseServiceStub();
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

	public void drawAnalysedArchitecture(DrawingDetail detail) {
		AbstractDTO[] modules = analyseService.getRootModules();
		drawModulesAndSubmodules(modules);
	}

	// FIXME: This function draws modules, their submodules and connects them.
	// It shouldn't.
	private void drawModulesAndSubmodules(AbstractDTO[] modules) {

		AnalysedModuleDTO[] castedModules = (AnalysedModuleDTO[]) modules;

		for (AnalysedModuleDTO dto : castedModules) {
			BaseFigure packageFigure = figureFactory.createFigure(dto);
			drawing.add(packageFigure);

			AnalysedModuleDTO[] subModules = analyseService
					.getChildModulesInModule(dto.uniqueName);
			if (subModules != null) {

				for (AnalysedModuleDTO subModule : subModules) {

					BaseFigure figure = figureFactory.createFigure(subModule);
					drawing.add(figure);

					drawing.add(connectionStrategy.connect(packageFigure, figure));
				}
			}
		}
	}

	public void drawDefinedArchitecture(DrawingDetail detail) {

	}

	@Override
	public void moduleZoom(BaseFigure selectedFigure) {
		System.out.println("Zooming in on " + selectedFigure.getName());
	}

	@Override
	public void moduleSelected(BaseFigure selectedFigure) {
		System.out.println("Selected module " + selectedFigure.getName());
	}
}
