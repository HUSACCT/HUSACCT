package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.graphics.presentation.decorators.DTODecorator;
import husacct.graphics.presentation.figures.BaseFigure;

import org.apache.log4j.Logger;

public class AnalysedController extends BaseController {

	private IAnalyseService analyseService;
	private Logger logger = Logger.getLogger(AnalysedController.class);

	public AnalysedController() {
		super();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
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

	private void drawModules(AbstractDTO[] modules) {
		AnalysedModuleDTO[] castedModules = (AnalysedModuleDTO[]) modules;
		this.clearDrawing();
		
		for (AnalysedModuleDTO dto : castedModules) {
			
			BaseFigure packageFigure = figureFactory.createFigure(dto);
			drawing.add(packageFigure);

			BasicLayoutStrategy bls = new BasicLayoutStrategy(drawing);
			bls.doLayout();
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
	public void moduleZoom(BaseFigure zoomedModuleFigure) {
		AbstractDTO dto = FigureResolver.resolveDTO(zoomedModuleFigure);
		if(dto.getClass().getSimpleName().equals("AnalysedModuleDTO")){
			AnalysedModuleDTO newdto = ((AnalysedModuleDTO)dto);
			this.setCurrentPath(newdto.uniqueName);
			getAndDrawModulesIn(newdto.uniqueName);
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

	@Override
	public void exportToImage() {
		// TODO Auto-generated method stub
		System.out.println("Option triggered: Export to image");
	}

	@Override
	public void toggleViolations() {
		// TODO Auto-generated method stub
		System.out.println("Option triggered: Toggle violations visiblity");
	}
}
