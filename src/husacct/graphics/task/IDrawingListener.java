package husacct.graphics.task;

import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;

public interface IDrawingListener
{
	public void onModuleZoom(ModuleDTO moduleDTO);
	public void onViolationsSelected(ViolationDTO[] violationsDTOs);
}
