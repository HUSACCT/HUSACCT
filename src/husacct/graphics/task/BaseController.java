package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.GraphicsFrame;
import husacct.graphics.presentation.decorators.DTODecorator;
import husacct.graphics.presentation.decorators.Decorator;
import husacct.graphics.presentation.decorators.ViolationsDecorator;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.FigureFactory;
import husacct.validate.IValidateService;
import husacct.validate.ValidateServiceStub;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JInternalFrame;

import org.jhotdraw.draw.ConnectionFigure;

public abstract class BaseController implements MouseClickListener {
	protected Drawing drawing;
	protected DrawingView view;
	protected GraphicsFrame drawTarget;
	protected String currentPath;
	protected boolean showViolations = false;

	protected FigureFactory figureFactory;
	protected FigureConnectorStrategy connectionStrategy;
	protected HashMap<AbstractDTO, BaseFigure> dtoFigureMap = new HashMap<AbstractDTO, BaseFigure>();

	public BaseController() {

		figureFactory = new FigureFactory();
		connectionStrategy = new FigureConnectorStrategy();

		initializeComponents();
	}

	private void initializeComponents() {
		drawing = new Drawing();
		view = new DrawingView(drawing);
		view.addListener(this);

		drawTarget = new GraphicsFrame(view);
		drawTarget.addListener(this);
	}

	public JInternalFrame getGUI() {
		return drawTarget;
	}

	public void clearDrawing() {
		this.drawing.clear();
		this.view.clearSelection();
	}

	public String getCurrentPath() {
		return this.currentPath;
	}

	public void resetCurrentPath() {
		this.currentPath = "";
	}

	public void setCurrentPath(String path) {
		this.currentPath = path;
	}

	@Override
	public void figureSelected(BaseFigure clickedFigure) {
		if (clickedFigure instanceof ViolationsDecorator) {
			this.drawTarget.showViolations(((ViolationsDecorator) clickedFigure).getViolations());
		}
		// do recursion here, because the the decorators
		// we are checking for above may be hidden behind
		// other decorators
		else if (clickedFigure instanceof Decorator) {
			this.figureSelected(((Decorator) clickedFigure).getDecorator());
		} else {
			this.drawTarget.hidePropertiesPane();
		}
	}

	@Override
	public void figureDeselected() {
		this.drawTarget.hidePropertiesPane();
	}

	public abstract void drawArchitecture(DrawingDetail detail);

	protected void drawModules(AbstractDTO[] modules) {
		this.clearDrawing();
		this.dtoFigureMap.clear();
		for (AbstractDTO dto : modules) {
			BaseFigure generatedFigure = figureFactory.createFigure(dto);
			drawing.add(generatedFigure);
			this.dtoFigureMap.put(dto, generatedFigure); // TODO: Check with team if their findbyname in drawingview is stable or not.

			BasicLayoutStrategy bls = new BasicLayoutStrategy(drawing);
			bls.doLayout();
		}
		this.drawTarget.setCurrentPathInfo(this.currentPath);
	}

	public void drawViolationsForShownModules() {
		// TODO retrieve the real service from the ServiceProvider instead of using the stub
		IValidateService validateService = new ValidateServiceStub();

		ArrayList<DTODecorator> moduleFigures = new ArrayList<DTODecorator>();
		for (BaseFigure f : this.drawing.getShownModules()) {
			if (f instanceof DTODecorator) {
				moduleFigures.add((DTODecorator) f);
			}
		}

		for (DTODecorator moduleFigureFrom : moduleFigures) {
			for (DTODecorator moduleFigureTo : moduleFigures) {
				AbstractDTO dtoFrom = moduleFigureFrom.getDTO();
				AbstractDTO dtoTo = moduleFigureTo.getDTO();

				if ((dtoFrom instanceof ModuleDTO) && (dtoTo instanceof ModuleDTO)) {
					ViolationDTO[] violationDTOs = validateService.getViolationsByLogicalPath(((ModuleDTO) dtoFrom).logicalPath,((ModuleDTO) dtoTo).logicalPath);					if (violationDTOs.length > 0) {
					this.drawViolations(violationDTOs, moduleFigureFrom,moduleFigureTo);
					}
				}
				// TODO AnalysedModuleDTO
			}
		}
	}

	private void drawViolations(ViolationDTO[] violationDTOs,
			BaseFigure fromFigure, BaseFigure toFigure) {
		BaseFigure violatedRelationFigure = this.figureFactory
				.createFigure(violationDTOs);
		this.connectionStrategy.connect(
		// TODO a very ugly cast here
				(ConnectionFigure) ((Decorator) violatedRelationFigure)
						.getDecorator(), fromFigure, toFigure);

		this.drawing.add(violatedRelationFigure);
	}

	public void toggleViolations() {
		showViolations = (showViolations ? false : true);
	}

	protected boolean showViolations() {
		return showViolations;
	}

	protected DrawingDetail getCurrentDrawingDetail(){
		DrawingDetail detail = DrawingDetail.WITHOUT_VIOLATIONS;
		if(showViolations()){
			detail = DrawingDetail.WITH_VIOLATIONS;
		}
		return detail;
	}
}
