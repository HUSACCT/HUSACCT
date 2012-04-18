package husacct.graphics.task;

import husacct.analyse.AnalyseServiceStub;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
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
	public void moduleZoom(AbstractDTO zoomedModuleDTO) {
		System.out.println("child " + zoomedModuleDTO.getClass());
		
		if (zoomedModuleDTO instanceof AnalysedModuleDTO) {
			// TODO clear drawing

			AnalysedModuleDTO analysedModuleDTO = (AnalysedModuleDTO) zoomedModuleDTO;

			AnalysedModuleDTO[] childModules = this.analyseService
					.getChildModulesInModule(analysedModuleDTO.uniqueName);

			for (AnalysedModuleDTO childModule : childModules) {
				System.out.println("child " + childModule.name);
			}
		}
	}
}
