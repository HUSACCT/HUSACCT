package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.GraphicsFrame;
import husacct.graphics.presentation.decorators.Decorator;
import husacct.graphics.presentation.decorators.DependenciesDecorator;
import husacct.graphics.presentation.decorators.ViolationsDecorator;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.FigureFactory;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JInternalFrame;

public abstract class BaseController implements MouseClickListener {
	
	private final int ITEMS_PER_ROW = 4;
	
	protected Drawing drawing;
	protected DrawingView view;
	protected GraphicsFrame drawTarget;
	protected String currentPath;
	protected boolean showViolations = false;

	protected FigureFactory figureFactory;
	protected FigureConnectorStrategy connectionStrategy;
	protected HashMap<BaseFigure, AbstractDTO> figureDTOMap = new HashMap<BaseFigure, AbstractDTO>();
	protected BasicLayoutStrategy layoutStrategy;

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
		
		layoutStrategy = new BasicLayoutStrategy(drawing);
	}

	public JInternalFrame getGUI() {
		return drawTarget;
	}

	public void clearDrawing() {
		this.figureDTOMap.clear();
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
	
	public AbstractDTO getDTOFromFigure(BaseFigure figure){
		return this.figureDTOMap.get(figure);
	}

	public abstract void drawArchitecture(DrawingDetail detail);

	protected void drawModules(AbstractDTO[] modules) {
		this.clearDrawing();
		for (AbstractDTO dto : modules) {
			BaseFigure generatedFigure = figureFactory.createFigure(dto);
			drawing.add(generatedFigure);
			this.figureDTOMap.put(generatedFigure, dto);

			BasicLayoutStrategy bls = new BasicLayoutStrategy(drawing);
			bls.doLayout(ITEMS_PER_ROW);
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
