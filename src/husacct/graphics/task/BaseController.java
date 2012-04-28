package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.GraphicsFrame;
import husacct.graphics.presentation.decorators.DTODecorator;
import husacct.graphics.presentation.decorators.Decorator;
import husacct.graphics.presentation.decorators.DependenciesDecorator;
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
	
	protected HashMap<BaseFigure, AbstractDTO> figureDTOMap = new HashMap<BaseFigure, AbstractDTO>();

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
	public void figureSelected(BaseFigure[] figures) {
		// FIXME: Wrap these instanceof calls into a (series of abstract)
		// clas(ses) and remove the
		// recursion that is happening. Discuss solution with team before
		// implementing
		BaseFigure selectedFigure = figures[0];

		if (selectedFigure instanceof ViolationsDecorator) {
			this.drawTarget.showViolationsProperties(((ViolationsDecorator) selectedFigure).getViolations());
		} else if (selectedFigure instanceof DependenciesDecorator) {
			this.drawTarget.showDependenciesProperties(((DependenciesDecorator) selectedFigure).getDependencies());
		}

		else if (selectedFigure instanceof Decorator) {
			Decorator decorator = (Decorator) selectedFigure;

			ArrayList<BaseFigure> list = new ArrayList<BaseFigure>();
			list.add(decorator.getDecorator());

			figureSelected((BaseFigure[]) list.toArray(new BaseFigure[list.size()]));

		} else {

			drawTarget.hidePropertiesPane();
		}
	}

	@Override
	public void figureDeselected(BaseFigure[] figures) {
		
		if (view.getSelectionCount() == 0) {
			drawTarget.hidePropertiesPane();
		}		
	}

	public abstract void drawArchitecture(DrawingDetail detail);

	protected void drawModules(AbstractDTO[] modules) {
		this.clearDrawing();
		this.figureDTOMap.clear();
		for (AbstractDTO dto : modules) {
			BaseFigure generatedFigure = figureFactory.createFigure(dto);
			drawing.add(generatedFigure);
			this.figureDTOMap.put(generatedFigure, dto);

			BasicLayoutStrategy bls = new BasicLayoutStrategy(drawing);
			bls.doLayout();
		}
		this.drawTarget.setCurrentPathInfo(this.currentPath);
	}

	public void toggleViolations() {
		showViolations = (showViolations ? false : true);
	}
	public boolean violationsAreShown(){
		return showViolations;
	}
	public boolean dependenciesAreShown(){
		return !violationsAreShown();
	}
	public void showViolations(){
		showViolations = true;
	}

	protected DrawingDetail getCurrentDrawingDetail() {
		DrawingDetail detail = DrawingDetail.WITHOUT_VIOLATIONS;
		if(violationsAreShown()){
			detail = DrawingDetail.WITH_VIOLATIONS;
		}
		return detail;
	}
}
