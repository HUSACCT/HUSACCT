package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ModuleDTO;


public class DefinedController extends BaseController
{
	public DefinedController() {
		super();
	}
	
	public void drawArchitecture(DrawingDetail detail) { 
	}
	
	@Override
	public void moduleZoom(AbstractDTO zoomedModuleDTO)
	{
		if(zoomedModuleDTO instanceof ModuleDTO)
		{
			System.out.println("Zooming in on "+((ModuleDTO)zoomedModuleDTO).logicalPath);
		}
	}

}
