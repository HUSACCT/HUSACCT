package husacct.graphics.task;

import husacct.analyse.AnalyseServiceStub;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.graphics.presentation.decorators.DTODecorator;
import husacct.graphics.presentation.figures.BaseFigure;

public class AnalysedController extends BaseController {

	private IAnalyseService analyseService;

	public AnalysedController() {
		super();

		analyseService = new AnalyseServiceStub();
	}

	public void drawArchitecture(DrawingDetail detail) {

		AbstractDTO[] modules = analyseService.getRootModules();
		drawModules(modules);
	}

	private void drawModules(AbstractDTO[] modules) {
		AnalysedModuleDTO[] castedModules = (AnalysedModuleDTO[]) modules;

		for (AnalysedModuleDTO dto : castedModules) {
			BaseFigure packageFigure = figureFactory.createFigure(dto);
			drawing.add(packageFigure);
		}
	}

	@Override
	public void moduleZoom(BaseFigure zoomedModuleFigure) {
		AbstractDTO dto = ((DTODecorator) zoomedModuleFigure).getDTO();
		switch(dto.getClass().getSimpleName()){
			case "AnalysedModuleDTO":
				AnalysedModuleDTO newdto = ((AnalysedModuleDTO)dto);
				AnalysedModuleDTO[] children = analyseService.getChildModulesInModule(newdto.uniqueName);
				drawModules(children);
		}
	}
}
