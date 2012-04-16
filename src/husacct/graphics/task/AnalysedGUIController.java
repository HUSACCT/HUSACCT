package husacct.graphics.task;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;

public class AnalysedGUIController extends AbstractGUIController
{
	
	public void drawAnalysedArchitecture(DrawingDetail detail)
	{
		/*
		 * TODO
		 * 
		 * contact the AnalyseService,
		 * get the moduleDTOs,
		 * call
		 */
		this.drawModules(new ModuleDTO[]{});
		/*
		 * get the dependencyDTOs
		 * call
		 */
		this.drawRelations(new DependencyDTO[]{});
		
		if(detail==DrawingDetail.WITH_VIOLATIONS)
		{
			drawViolationsForShownModules();
		}
	}
	
	private void drawRelations(DependencyDTO[] dependencyDTOs)
	{
		//TODO
	}
}
