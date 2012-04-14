package husacct.graphics.task;

import husacct.common.dto.ModuleDTO;

public class DefinedGUIController extends AbstractGUIController
{
	
	public void drawDefinedArchitecture(DrawingDetail detail)
	{ 
		/*
		 * TODO
		 * 
		 * contact the DefineService,
		 * get the moduleDTOs,
		 * call
		 */
		this.drawModules(new ModuleDTO[]{});
		
		
		if(detail==DrawingDetail.WITH_VIOLATIONS)
		{
			drawViolationsForShownModules();
		}
	}

}
