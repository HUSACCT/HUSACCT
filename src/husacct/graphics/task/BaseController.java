package husacct.graphics.task;

import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.GraphicsFrame;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.FigureFactory;
import husacct.graphics.presentation.figures.RelationFigure;
import husacct.validate.IValidateService;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;

public abstract class BaseController implements MouseClickListener {
	
	private final int ITEMS_PER_ROW = 4;
		
	protected Drawing drawing;
	protected DrawingView view;
	protected GraphicsFrame drawTarget;
	protected String currentPath = "";
	private boolean showViolations = false;
	
	protected Logger logger = Logger.getLogger("Graphics Controller");

	protected FigureFactory figureFactory;
	protected FigureConnectorStrategy connectionStrategy;
	private HashMap<BaseFigure, AbstractDTO> figureDTOMap = new HashMap<BaseFigure, AbstractDTO>();
	private HashMap<RelationFigure, DependencyDTO[]> dependencyFigureMap = 
			new HashMap<RelationFigure, DependencyDTO[]>();
	private HashMap<RelationFigure, ViolationDTO[]> violationFigureMap = 
			new HashMap<RelationFigure, ViolationDTO[]>();
	private HashMap<BaseFigure, ViolationDTO[]> violatedModuleDTOMap = 
			new HashMap<BaseFigure, ViolationDTO[]>(); 
	protected BasicLayoutStrategy layoutStrategy;
	
	protected IAnalyseService analyseService;
	protected IValidateService validateService;

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
		this.dependencyFigureMap.clear();
		this.violatedModuleDTOMap.clear();
		this.violationFigureMap.clear();
		
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
		BaseFigure selectedFigure = figures[0];
		
		if(this.violatedModuleDTOMap.containsKey(selectedFigure)) {
			this.drawTarget.showViolationsProperties(this.violatedModuleDTOMap.get(selectedFigure));
		}
		else if (this.violationFigureMap.containsKey(selectedFigure)) {
			this.drawTarget.showViolationsProperties(this.violationFigureMap.get(selectedFigure));
		}
		else if (this.dependencyFigureMap.containsKey(selectedFigure)) {
			this.drawTarget.showDependenciesProperties(this.dependencyFigureMap.get(selectedFigure));
		}
		else {
			this.drawTarget.hidePropertiesPane();
		}
	}

	@Override
	public void figureDeselected(BaseFigure[] figures) {
		if (view.getSelectionCount() == 0) {
			drawTarget.hidePropertiesPane();
		}
	}
	
	public void linkDTOtoFigure(AbstractDTO dto, BaseFigure figure){
		this.figureDTOMap.put(figure, dto);
	}
	
	public AbstractDTO getDTOFromFigure(BaseFigure figure){
		return this.figureDTOMap.get(figure);
	}
	
	public void linkDependencyDTOsToFigure(DependencyDTO[] dtos, RelationFigure figure){
		this.dependencyFigureMap.put(figure, dtos);
	}
	
	public DependencyDTO[] getDependencyDTOsFromRelationFigure(RelationFigure fig) {
		return this.dependencyFigureMap.get(fig);
	}
	
	public void linkViolationDTOsToFigure(ViolationDTO[] dtos, RelationFigure figure) {
		this.violationFigureMap.put(figure, dtos);
	}
	
	public ViolationDTO[] getViolationDTOsFromRelationFigure(RelationFigure fig) {
		return this.violationFigureMap.get(fig);
	}
	
	public void linkViolationDTOsToModuleFigure(ViolationDTO[] dtos, BaseFigure fig) {
		this.violatedModuleDTOMap.put(fig, dtos);
	}
	
	public ViolationDTO[] getViolationsForModuleFigure(BaseFigure fig) {
		return this.violatedModuleDTOMap.get(fig);
	}

	public abstract void drawArchitecture(DrawingDetail detail);

	protected void drawModules(AbstractDTO[] modules) {
		this.clearDrawing();
		for (AbstractDTO dto : modules) {
			BaseFigure generatedFigure = figureFactory.createFigure(dto);
			drawing.add(generatedFigure);
			this.linkDTOtoFigure(dto, generatedFigure);

			BasicLayoutStrategy bls = new BasicLayoutStrategy(drawing);
			bls.doLayout(ITEMS_PER_ROW);
		}
		this.drawTarget.setCurrentPathInfo(this.currentPath);
	}

	public void toggleViolations() {
		if(showViolations) {
			Logger.getLogger(this.getClass()).debug("hiding violations");
			showViolations = false;
			
			// clear violations
			for(BaseFigure fig : this.violatedModuleDTOMap.keySet()) {
				fig.setViolated(false);
			}
			this.violatedModuleDTOMap.clear();
			for(BaseFigure fig : this.violationFigureMap.keySet()) {
				this.drawing.remove(fig);
			}
			this.violationFigureMap.clear();
		}
		else {
			Logger.getLogger(this.getClass()).debug("showing violations");
			showViolations = true;
			
			this.drawViolationsForShownModules();
		}
	}

	@Override
	public void exportToImage() {
		// TODO Make better
		this.drawing.showExportToImagePanel();
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
	
	// dependencies
	
	public void drawDependenciesForShownModules(){
		BaseFigure[] shownModules = this.drawing.getShownModules();
		for (BaseFigure figureFrom : shownModules) {
			for (BaseFigure figureTo : shownModules) {				
				getAndDrawDependenciesBetween(figureFrom, figureTo);
			}
		}
		sizeRelationFigures(this.dependencyFigureMap);
	}

	private void getAndDrawDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		DependencyDTO[] dependencies = (DependencyDTO[])getDependenciesBetween(figureFrom, figureTo);
		if(dependencies.length > 0) {
			RelationFigure dependencyFigure = this.figureFactory.createFigure(dependencies);
			this.linkDependencyDTOsToFigure(dependencies, dependencyFigure);
			this.connectionStrategy.connect(dependencyFigure, figureFrom, figureTo);
			drawing.add(dependencyFigure);
		}
	}
	
	protected abstract DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo);
	
	// violations
	
	public void drawViolationsForShownModules() {
		BaseFigure[] shownModules = this.drawing.getShownModules();
		for (BaseFigure figureFrom : shownModules) {
			for (BaseFigure figureTo : shownModules) {
				// are the violations in the same module?
				if(figureFrom == figureTo) {
					getAndDrawViolationsIn(figureFrom);
				}
				else {
					getAndDrawViolationsBetween(figureFrom, figureTo);
				}
			}
		}
		this.sizeRelationFigures(violationFigureMap);
	}
	
	private void getAndDrawViolationsIn(BaseFigure figureFrom) {
		ViolationDTO[] violations = getViolationsBetween(figureFrom, figureFrom);
		if(violations.length > 0) {
			figureFrom.setViolated(true);
			this.linkViolationDTOsToModuleFigure(violations, figureFrom);
		}
	}

	private void getAndDrawViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		ViolationDTO[] violations = getViolationsBetween(figureFrom, figureTo);
		if(violations.length > 0) {
			RelationFigure violationFigure = this.figureFactory.createFigure(violations);
			this.linkViolationDTOsToFigure(violations, violationFigure);
			this.connectionStrategy.connect(violationFigure, figureFrom, figureTo);
			drawing.add(violationFigure);
		}
	}
	
	protected abstract ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo);
	
	private void sizeRelationFigures(HashMap<RelationFigure, ? extends AbstractDTO[]> figures) {
		// 1 relation, small
		if(figures.size() == 1) {
			figures.keySet().iterator().next().setLineThickness(1);
		}
		// 2 relations; both small, or one slightly bigger 
		else if (figures.size() == 2) {
			Iterator<RelationFigure> iterator = figures.keySet().iterator();
			RelationFigure figure1 = iterator.next();
			RelationFigure figure2 = iterator.next();
			int length1 = figures.get(figure1).length;
			int length2 = figures.get(figure2).length;
			
			if(length1 == length2) {
				figure1.setLineThickness(1);
				figure2.setLineThickness(1);
			}
			else if (length1 < length2) {
				figure1.setLineThickness(1);
				figure2.setLineThickness(2);
			}
			else { // length1 > length2
				figure1.setLineThickness(2);
				figure2.setLineThickness(1);
			}
		}
		// 3 ore more relations; small, big or fat, according to scale
		else if (figures.size() >= 3) {
			// max amounts of dependencies
			int maxAmount = -1;
			for(RelationFigure fig : figures.keySet()) {
				int length = figures.get(fig).length;
				
				if(maxAmount == -1 || maxAmount < length) {
					maxAmount = length;
				}
			}
			
			// set line thickness according to scale
			for(RelationFigure fig : figures.keySet()) {
				double weight = (double)figures.get(fig).length/maxAmount;
				if(weight < 0.33) {
					fig.setLineThickness(1);
				} else if (weight < 0.66) {
					fig.setLineThickness(3);
				} else {
					fig.setLineThickness(4);
				}			
			}
		}
	}
}
