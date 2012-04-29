package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.graphics.presentation.decorators.Decorator;
import husacct.graphics.presentation.figures.BaseFigure;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.ConnectionFigure;

public class AnalysedController extends BaseController {

	private final int ITEMS_PER_ROW = 4;
	
	private IControlService controlService;
	private Logger logger = Logger.getLogger(AnalysedController.class);

	public AnalysedController() {
		super();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		controlService = ServiceProvider.getInstance().getControlService();

		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				getAndDrawModulesIn(getCurrentPath());
				if(violationsAreShown()){
					drawViolationsForShownModules();
				}
			}
		});
	}

	public void drawArchitecture(DrawingDetail detail) {
		AbstractDTO[] modules = analyseService.getRootModules();
		this.resetCurrentPath();
		this.drawModules(modules);
		
		if(detail == DrawingDetail.WITH_VIOLATIONS){
			this.showViolations();
		}
		this.drawLinesBasedOnSetting();
	}

	protected void drawModules(AbstractDTO[] modules) {
		super.drawModules(modules);
		layoutStrategy.doLayout(ITEMS_PER_ROW);
	}
	
	// Dependencies
	
	private void drawDependenciesForShownModules(){
		BaseFigure[] shownModules = this.drawing.getShownModules();
		for (BaseFigure figureFrom : shownModules) {
			for (BaseFigure figureTo : shownModules) {
				getAndDrawDependencyBetween(figureFrom, figureTo);
			}
		}
	}
	
	private void getAndDrawDependencyBetween(BaseFigure figureFrom, BaseFigure figureTo){
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) this.getDTOFromFigure(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) this.getDTOFromFigure(figureTo);
		
		try{
			DependencyDTO[] dependencies = getDependenciesBetween(dtoFrom.uniqueName, dtoTo.uniqueName);
			BaseFigure dependencyFigure = this.figureFactory.createFigure(dependencies);
			this.connectionStrategy.connect((ConnectionFigure) ((Decorator) dependencyFigure).getDecorator(), figureFrom, figureTo);
			drawing.add(dependencyFigure);
		} catch (RuntimeException e) {
			logger.debug(e.getMessage() + " " + dtoFrom.uniqueName + " -> " + dtoTo.uniqueName);
		}
	}
	
	// Violations
	
	public void drawViolationsForShownModules() {
		BaseFigure[] shownModules = this.drawing.getShownModules();
		for (BaseFigure figureFrom : shownModules) {
			for (BaseFigure figureTo : shownModules) {
				getAndDrawViolationBetween(figureFrom, figureTo);
			}
		}
	}
	
	private void getAndDrawViolationBetween(BaseFigure figureFrom, BaseFigure figureTo){
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) this.getDTOFromFigure(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) this.getDTOFromFigure(figureTo);
		
		try{
			ViolationDTO[] dependencies = getViolationsBetween(dtoFrom.uniqueName, dtoTo.uniqueName);
			BaseFigure violationFigure = this.figureFactory.createFigure(dependencies);
			this.connectionStrategy.connect((ConnectionFigure) ((Decorator) violationFigure).getDecorator(), figureFrom, figureTo);
			drawing.add(violationFigure);
		} catch (RuntimeException e) {
			logger.debug(e.getMessage() + " " + dtoFrom.uniqueName + " -> " + dtoTo.uniqueName);
		}
	}
	
	// Listener methods

	@Override
	public void moduleZoom(BaseFigure figure) {
		if (figure.isModule()) { //FIXME? : Can zoom only on modules
			AbstractDTO dto = this.getDTOFromFigure(figure);
	
			if (dto.getClass().getSimpleName().equals("AnalysedModuleDTO")) {
				AnalysedModuleDTO parentDTO = ((AnalysedModuleDTO) dto);
				getAndDrawModulesIn(parentDTO.uniqueName);
			}
		}
	}

	@Override
	public void moduleZoomOut() {
		AnalysedModuleDTO parentDTO = analyseService.getParentModuleForModule(this.getCurrentPath());
		if (null != parentDTO) {
			this.getAndDrawModulesIn(parentDTO.uniqueName);
		} else {
			logger.debug("Tried to zoom out from " + this.getCurrentPath() + ", but it has no parent.");
			logger.debug("Reverting to the root of the application.");
			drawArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
		}
	}

	private void getAndDrawModulesIn(String parentName) {
		this.setCurrentPath(parentName);
		AnalysedModuleDTO[] children = analyseService.getChildModulesInModule(parentName);
		if (children.length > 0) {
			this.drawModules(children);
			this.drawLinesBasedOnSetting();
		} else {
			logger.debug("Tried to draw modules for " + parentName + ", but it has no children.");
		}
	}

	@Override
	public void exportToImage() {
		// TODO Auto-generated method stub
		System.out.println("Option triggered: Export to image");
	}

	@Override
	public void toggleViolations() {
		super.toggleViolations();
		this.drawLinesBasedOnSetting();
	}
	
	private void drawLinesBasedOnSetting(){
		this.drawing.clearLines();
		if(violationsAreShown()){
			this.drawViolationsForShownModules();
		}else{
			this.drawDependenciesForShownModules();
		}
	}
}
