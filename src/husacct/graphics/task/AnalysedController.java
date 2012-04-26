package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.graphics.presentation.decorators.Decorator;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.validate.IValidateService;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.ConnectionFigure;

public class AnalysedController extends BaseController {

	private IControlService controlService;
	private IAnalyseService analyseService;
	private IValidateService validateService;
	private Logger logger = Logger.getLogger(AnalysedController.class);

	public AnalysedController() {
		super();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		controlService = ServiceProvider.getInstance().getControlService();
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				//TODO: Redraw entire drawing for locale change
				System.out.println("Graphics: Language change.");
			}
		});
	}

	public void drawArchitecture(DrawingDetail detail) {
		AbstractDTO[] modules = analyseService.getRootModules();
		this.resetCurrentPath();
		drawModules(modules);
		
		if(detail == DrawingDetail.WITH_VIOLATIONS)
		{
			this.drawViolationsForShownModules();
		}
	}
	
	private DependencyDTO[] getDependenciesBetween(String from, String to){
		return analyseService.getDependencies(from, to);
	}

	protected void drawModules(AbstractDTO[] modules) {
		super.drawModules(modules);
		this.drawDependencies(modules);
	}
	
	private void drawDependencies(AbstractDTO[] modules){
		AnalysedModuleDTO[] analysedModules = (AnalysedModuleDTO[]) modules; 
		for(AnalysedModuleDTO analysedModuleDTO : analysedModules){
			for(AnalysedModuleDTO innerAnalysedModuleDTO : analysedModules){
				DependencyDTO[] dependencies = getDependenciesBetween(analysedModuleDTO.uniqueName, innerAnalysedModuleDTO.uniqueName);
				
				try{
					BaseFigure dependencyFigure = this.figureFactory.createFigure(dependencies);
					this.connectionStrategy.connect(
							(ConnectionFigure)((Decorator)dependencyFigure).getDecorator(), 
							this.dtoFigureMap.get(analysedModuleDTO), 
							this.dtoFigureMap.get(innerAnalysedModuleDTO));
					drawing.add(dependencyFigure);
				}catch(RuntimeException e){
					logger.debug(e.getMessage()+" "+analysedModuleDTO.uniqueName+" -> "+innerAnalysedModuleDTO.uniqueName);
				}
			}
		}
	}

	@Override
	public void moduleZoom(BaseFigure zoomedModuleFigure) {
		AbstractDTO dto = FigureResolver.resolveDTO(zoomedModuleFigure);
		if(dto.getClass().getSimpleName().equals("AnalysedModuleDTO")){
			AnalysedModuleDTO analysedDTO = ((AnalysedModuleDTO)dto);
			this.setCurrentPath(analysedDTO.uniqueName);
			getAndDrawModulesIn(analysedDTO.uniqueName);
		}
	}

	@Override
	public void moduleZoomOut() {
		AnalysedModuleDTO parentDTO = analyseService.getParentModuleForModule(this.getCurrentPath());
		if(parentDTO!=null){
			this.setCurrentPath(parentDTO.uniqueName);
			getAndDrawModulesIn(parentDTO.uniqueName);
		}else{
			logger.debug("Tried to zoom out from "+this.getCurrentPath()+", but it has no parent.");
			logger.debug("Reverting to the root of the application.");
			drawArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
		}
	}
	
	private void getAndDrawModulesIn(String parentName) {
		AnalysedModuleDTO[] children = analyseService.getChildModulesInModule(parentName);
		if (children.length > 0) {
			drawModules(children);
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
		System.out.println("Option triggered: Toggle violations visiblity");
		//drawArchitecture(DrawingDetail detail) <- use
		if(showViolations()){
			//TODO
			// Loop through all the figures/dtos
			// Request found violations between all combinations
			// Create a relationFigure for the violations
			// Clear dependency lines
			// Add the violation lines to the drawing
			//validateService.getViolationsByPhysicalPath(physicalpathFrom, physicalpathTo);
		}else{
			// TODO
			// Loop through all the figures/dtos
			// Request found dependencies between all combinations
			// Create a relationFigure for the dependencies
			// Clear violation lines
			// Add the dependency lines to the drawing
			//this.drawDependencies(modules); //Where to get the modules? We do not save them!
		}
	}
}
